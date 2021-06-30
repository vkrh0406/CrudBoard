package crud.board.controller;


import crud.board.controller.form.BoardForm;
import crud.board.domain.Board;
import crud.board.dto.BoardDto;
import crud.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.rule.Mode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Slf4j
public class BoardController {

    private final BoardService boardService;

    //게시글 폼 생성
    @GetMapping("board/new")
    public String createBoardForm(Model model) {
        model.addAttribute("boardForm", new BoardForm());

        return "board/createdBoardForm";
    }

    //게시글 생성
    @PostMapping("board/new")
    public String createBoard(@Valid BoardForm boardForm, BindingResult result,Model model) {
        if (result.hasErrors()) {
            return "/board/createdBoardForm";
        }

        Board board = new Board(boardForm.getTitle(), boardForm.getWriter(), boardForm.getContent(),boardForm.getPassword());
        Long boardId = boardService.CreateBoard(board);


        
        return "redirect:/board/"+boardId;

    }

    //게시글 조회
    @GetMapping("board/{boardId}")
    public String lookBoard(@PathVariable("boardId") Long id, Model model) {


        System.out.println("id = " + id);

       // model.setViewName("boardContent");

        Board findOne = boardService.findOne(id);
        BoardDto boardDto = new BoardDto(findOne.getId(), findOne.getTitle(), findOne.getWriter(), findOne.getContent(),findOne.getCreatedTime());

        model.addAttribute("boardDto", boardDto);


        return "board/boardContent";
    }

    //게시글 리스트
    @GetMapping("board")
    public String boardList(Model model, BoardSearch boardSearch,@PageableDefault(size = 10) Pageable pageable) {



        //검색
        Page<BoardDto> searchResults = boardService.search(boardSearch, pageable);



        model.addAttribute("boardSearch", boardSearch);
        model.addAttribute("boardDto", searchResults);

        return "board/boardList";

    }//게시글 리스트
    @GetMapping("board/api")
    @ResponseBody
    public Page<BoardDto> boardListApi(Model model, BoardSearch boardSearch,@PageableDefault(size = 10) Pageable pageable) {



        //검색
        Page<BoardDto> searchResults = boardService.search(boardSearch, pageable);




        return searchResults;

    }

    //게시글 수정 폼
    @GetMapping("board/edit/{id}")
    public String updateBoardForm(@PathVariable("id")Long id,Model model) {

        Board findOne = boardService.findOne(id);
        BoardForm boardForm = new BoardForm();
        boardForm.setTitle(findOne.getTitle());
        boardForm.setWriter(findOne.getWriter());
        boardForm.setContent(findOne.getContent());
        boardForm.setId(findOne.getId());


        model.addAttribute("boardForm", boardForm);


        return "board/boardEditForm";

    }

    //게시글 수정
    @PostMapping("board/edit/{id}")
    public String updateBoard(@PathVariable("id")Long id, @Valid BoardForm boardForm) {

        System.out.println("boardForm = " + boardForm.getId());

        Board findOne = boardService.findOne(boardForm.getId());

        if(!boardForm.getPassword().equals(findOne.getPassword())){
            return "redirect:/board/edit/" + boardForm.getId();
        }


        boardService.update(findOne.getId(),boardForm.getTitle(),boardForm.getContent());

        return "redirect:/board";
    }

    //게시글 삭제
    @PostMapping("board/delete/{id}")
    public String deleteBoard(@PathVariable("id") Long id, @RequestParam("password") String password, RedirectAttributes redirectAttributes) throws IllegalAccessException {



        try {
            boardService.delete(id, password);
        } catch (Exception e) {
            redirectAttributes.addAttribute("id", id);
            log.debug(e.getMessage());

            return "redirect:/board/{id}";
        }


        return "redirect:/board";

    }
}
