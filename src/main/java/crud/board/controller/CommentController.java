package crud.board.controller;


import crud.board.argumentresolver.Login;
import crud.board.controller.form.CommentForm;
import crud.board.domain.Comment;
import crud.board.domain.Member;
import crud.board.service.BoardService;
import crud.board.service.CommentService;
import crud.board.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@Slf4j
@RequiredArgsConstructor
public class CommentController {
    private final BoardService boardService;
    private final MemberService memberService;
    private final CommentService commentService;

    @PostMapping("/comment/new")
    public String saveComment(@Login Member member, Model model, @ModelAttribute @Valid CommentForm commentForm, BindingResult bindingResult, @RequestParam Long boardId,
    RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addAttribute("id", boardId);
            return "redirect:/board/{id}";
        }

        //작성자가 익명이 아닌경우
        if (member != null) {
            commentService.saveComment(boardId, member, commentForm);

            redirectAttributes.addAttribute("id", boardId);
            return "redirect:/board/{id}";
        }

        commentService.saveComment(boardId, commentForm);

        redirectAttributes.addAttribute("id", boardId);
        return "redirect:/board/{id}";

    }

    @GetMapping("comment/delete/{id}")
    public String deleteComment(@PathVariable Long id, @RequestParam Long boardId,@Login Member member,
                                RedirectAttributes redirectAttributes) {

        if (member != null) {
            commentService.deleteComment(id,member,null);
            redirectAttributes.addAttribute("boardId", boardId);
            return "redirect:/board/{boardId}";
        }

        redirectAttributes.addAttribute("boardId", boardId);
        return "redirect:/board/{boardId}";

    }
}
