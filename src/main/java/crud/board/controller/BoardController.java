package crud.board.controller;


import crud.board.FileStore;
import crud.board.SessionConst;
import crud.board.argumentresolver.Login;
import crud.board.controller.form.BoardForm;
import crud.board.domain.Board;
import crud.board.domain.Member;
import crud.board.domain.UploadFile;
import crud.board.dto.BoardDto;
import crud.board.dto.MemberLoginDto;
import crud.board.service.BoardService;
import crud.board.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.rule.Mode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriUtils;

import javax.validation.Valid;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Slf4j
public class BoardController {

    private final BoardService boardService;
    private final FileStore fileStore;
    private final MemberService memberService;

    //게시글 폼 생성
    @GetMapping("board/new")
    public String createBoardForm(Model model, @Login Member member) {

        BoardForm boardForm = new BoardForm();
        if (member != null) {
            boardForm.setWriter(member.getUsername());
            boardForm.setPassword("0000");
            model.addAttribute("username", member.getUsername());
        }
        model.addAttribute("boardForm", boardForm);

        return "board/createdBoardForm";
    }

    //게시글 생성
    @PostMapping("board/new")
    public String createBoard(@Valid BoardForm boardForm, BindingResult result, Model model, @Login Member member) {
        if (result.hasErrors()) {
            return "/board/createdBoardForm";
        }

        Board board = new Board(boardForm.getTitle(), boardForm.getWriter(), boardForm.getContent(), boardForm.getPassword());

        if (member != null) {
            board.setMember(member);
        }


        List<MultipartFile> multipartFiles = boardForm.getMultipartFiles();

        List<UploadFile> uploadFiles = fileStore.storeFiles(multipartFiles);
        board.setUploadFiles(uploadFiles);


        Long boardId = boardService.CreateBoard(board);


        return "redirect:/board/" + boardId;

    }

    //게시글 조회
    @GetMapping("board/{boardId}")
    public String lookBoard(@PathVariable("boardId") Long id, Model model, @Login Member member) {

        log.info("member = {} ", member);

        Board findOne = boardService.findOne(id);

        //로그인 상태일시 멤버가 null이 아님
        if (member != null) {
            MemberLoginDto memberLoginDto = new MemberLoginDto();
            memberLoginDto.setUsername(member.getUsername());
            model.addAttribute("username", memberLoginDto.getUsername());

            //게시글의 멤버가 null이 아닌 동시에 세션 멤버와 id가 같으면 자기가 쓴 글
            if (findOne.getMember() != null && findOne.getMember().getId().equals(member.getId())) {
                model.addAttribute("myBoard", "myBoard");
            }


            log.info("username {}", memberLoginDto.getUsername());
        }


        System.out.println("id = " + id);

        // model.setViewName("boardContent");


        BoardDto boardDto = new BoardDto(findOne.getId(), findOne.getTitle(), findOne.getWriter(), findOne.getContent(), findOne.getCreatedTime());
        boardDto.setUploadFiles(findOne.getUploadFiles());


        model.addAttribute("boardDto", boardDto);


        return "board/boardContent";
    }

    //파일 다운로드
    @GetMapping("/attach/{boardId}")
    public ResponseEntity<Resource> downloadAttach(@PathVariable Long boardId,
                                                   @RequestParam String storeFileName) throws MalformedURLException {

        String uploadFileName = null;

        Board one = boardService.findOne(boardId);

        List<UploadFile> uploadFiles = one.getUploadFiles();
        for (UploadFile uploadFile : uploadFiles) {
            if (uploadFile.getStoreFileName().equals(storeFileName)) {
                uploadFileName = uploadFile.getUploadFileName();
            }
        }

        if (uploadFileName == null) {
            return null;
        }
        UrlResource urlResource = new UrlResource("file:" + fileStore.getFullPath(storeFileName));

        String encodedUploadFileName = UriUtils.encode(uploadFileName, StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=\"" + encodedUploadFileName + "\"";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(urlResource);


    }

    //게시글 리스트
    @GetMapping("board")
    public String boardList(Model model, BoardSearch boardSearch, @PageableDefault(size = 10) Pageable pageable,
                            @Login Member member) {

        log.info("member = {} ", member);

        if (member != null) {
            MemberLoginDto memberLoginDto = new MemberLoginDto();
            memberLoginDto.setUsername(member.getUsername());
            model.addAttribute("username", memberLoginDto.getUsername());
            log.info("username {}", memberLoginDto.getUsername());
        }


        //검색
        Page<BoardDto> searchResults = boardService.search(boardSearch, pageable);


        model.addAttribute("boardSearch", boardSearch);
        model.addAttribute("boardDto", searchResults);

        return "board/boardList";

    }//게시글 리스트

    @GetMapping("board/api")
    @ResponseBody
    public Page<BoardDto> boardListApi(Model model, BoardSearch boardSearch, @PageableDefault(size = 10) Pageable pageable) {


        //검색
        Page<BoardDto> searchResults = boardService.search(boardSearch, pageable);


        return searchResults;

    }

    //게시글 수정 폼
    @GetMapping("board/edit/{id}")
    public String updateBoardForm(@PathVariable("id") Long id, Model model, @Login Member member) {

        Board findOne = boardService.findOne(id);
        BoardForm boardForm = new BoardForm();

        //로그인 유무
        if (member != null) {
            model.addAttribute("username", member.getUsername());
        }

        //자기 게시글 유무
        if (member != null && findOne.getMember() != null && findOne.getMember().getId().equals(member.getId())) {
            model.addAttribute("myBoard", "myBoard");
            boardForm.setPassword("0000");
        }


        boardForm.setTitle(findOne.getTitle());
        boardForm.setWriter(findOne.getWriter());
        boardForm.setContent(findOne.getContent());
        boardForm.setId(findOne.getId());


        model.addAttribute("boardForm", boardForm);


        return "board/boardEditForm";

    }

    //게시글 수정
    @PostMapping("board/edit/{id}")
    public String updateBoard(@PathVariable("id") Long id, @Valid BoardForm boardForm, @Login Member member, RedirectAttributes redirectAttributes) {


        Board findOne = boardService.findOne(boardForm.getId());

        if (member != null && findOne.getMember() != null && findOne.getMember().getId().equals(member.getId())) {
            boardService.update(findOne.getId(), boardForm.getTitle(), boardForm.getContent());
            redirectAttributes.addAttribute("boardId", boardForm.getId());
            return "redirect:/board/{boardId}";
        }


        if (!boardForm.getPassword().equals(findOne.getPassword()) && findOne.getMember() == null) {
            return "redirect:/board/edit/" + boardForm.getId();
        }


        boardService.update(findOne.getId(), boardForm.getTitle(), boardForm.getContent());

        return "redirect:/board";
    }

    //게시글 삭제 (비밀번호로)
    @PostMapping("board/delete/{id}")
    public String deleteBoard(@PathVariable("id") Long id, @RequestParam("password") String password,
                              RedirectAttributes redirectAttributes) throws IllegalAccessException {


        try {
            boardService.delete(id, password);
        } catch (Exception e) {
            redirectAttributes.addAttribute("id", id);
            log.debug(e.getMessage());

            return "redirect:/board/{id}";
        }


        return "redirect:/board";

    }

    //게시글 삭제 (자기글 삭제)
    @PostMapping("board/delete/{id}/my")
    public String deleteMyBoard(@PathVariable("id") Long id, @RequestParam("password") String password,
                                @Login Member member,
                                RedirectAttributes redirectAttributes) throws IllegalAccessException {


        if (member != null) {

            try {
                boardService.delete(id, member);
            } catch (Exception e) {
                redirectAttributes.addAttribute("id", id);
                log.debug(e.getMessage());

                return "redirect:/board/{id}";
            }
        }


        return "redirect:/board";

    }

    @GetMapping("/board/myBoards")
    public String findMyBoards(Model model, BoardSearch boardSearch, @PageableDefault(size = 10) Pageable pageable,
                               @Login Member member) {
        if (member == null) {
            return "redirect:/";
        }

        model.addAttribute("username", member.getUsername());

        Member findMember = memberService.findMemberById(member.getId());

        List<Board> boards = findMember.getBoards();
        List<BoardDto> boardDto = boards.stream()
                .map(o -> new BoardDto(o.getId(), o.getTitle(), o.getWriter(), o.getContent(), o.getUpdatedTime()))
                .collect(Collectors.toList());

        model.addAttribute("boardDto", boardDto);

        return "/board/myBoards";


    }
}
