package crud.board.controller;


import crud.board.controller.form.BoardForm;
import crud.board.domain.Board;
import crud.board.dto.BoardDto;
import crud.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.dom4j.rule.Mode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping("board/new")
    public String createBoardForm(Model model) {
        model.addAttribute("boardForm", new BoardForm());

        return "board/createdBoardForm";
    }

    @PostMapping("board/new")
    public String createBoard(@Valid BoardForm boardForm, BindingResult result,Model model) {
        if (result.hasErrors()) {
            return "/board/createdBoardForm";
        }

        Board board = new Board(boardForm.getTitle(), boardForm.getWriter(), boardForm.getContent(),boardForm.getPassword());
        Long boardId = boardService.CreateBoard(board);




        return "redirect:/board/"+boardId;

    }

    @GetMapping("board/{boardId}")
    public String lookBoard(@PathVariable("boardId") Long id, Model model) {


        System.out.println("id = " + id);

       // model.setViewName("boardContent");

        Board findOne = boardService.findOne(id);
        BoardDto boardDto = new BoardDto(findOne.getId(), findOne.getTitle(), findOne.getWriter(), findOne.getContent(),findOne.getCreatedTime());

        model.addAttribute("boardDto", boardDto);


        return "board/boardContent";
    }

    @GetMapping("board")
    public String boardList(Model model) {

        List<Board> boardList = boardService.findBoardList();
        List<BoardDto> boardDto = boardList.stream()
                .map(o -> new BoardDto(o.getId(), o.getTitle(), o.getWriter(), o.getContent(), o.getUpdatedTime()))
                .collect(Collectors.toList());

        model.addAttribute("boardDto", boardDto);

        return "board/boardList";

    }

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

    @PostMapping("board/delete/{id}")
    public String deleteBoard(@PathVariable("id") Long id, @RequestParam("password") String password) {

        Board findOne = boardService.findOne(id);

        //패스워드가 다를시 게시글로 돌아옴
        if (!findOne.getPassword().equals(password)) {
            return "redirect:/board/" + id;
        }

        //같으면 삭제
        boardService.delete(findOne);

        return "redirect:/board";

    }
}
