package crud.board.controller;

import crud.board.SessionConst;
import crud.board.controller.form.LoginForm;
import crud.board.controller.form.MemberForm;
import crud.board.domain.Address;
import crud.board.domain.Member;
import crud.board.service.MemberService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@Slf4j
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/new")
    public String createMemberForm(@ModelAttribute MemberForm memberForm) {

        return "member/createMemberForm";
    }

    @PostMapping("/new")
    public String createMember(@ModelAttribute MemberForm memberForm, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            return "member/createMemberForm";
        }

        Member member = new Member(memberForm.getLoginId(), memberForm.getPassword(), memberForm.getName(), memberForm.getAge(),
                new Address(memberForm.getCity(), memberForm.getStreet(), memberForm.getZipcode()));

        memberService.saveMember(member);


        return "redirect:/board";

    }

    @GetMapping("/login")
    public String loginForm(@ModelAttribute LoginForm loginForm)
    {
        return "member/loginForm";
    }

    @PostMapping("/login")
    public String loginMember(@ModelAttribute LoginForm loginForm, BindingResult bindingResult,
                              HttpServletRequest request, @RequestParam(defaultValue = "/") String requestURI) {

        if (bindingResult.hasErrors()) {
            return "member/loginForm";
        }
        Member loginMember = memberService.login(loginForm.getLoginId(), loginForm.getPassword());

        if (loginMember == null) {
            bindingResult.reject("LoginFail","아이디나 비밀번호가 틀립니다");
            return "member/loginForm";
        }

        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.SESSION_LOGIN_MEMBER, loginMember);


        return "redirect:" + requestURI;
    }

    @GetMapping("/logout")
    public String logoutMember(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.removeAttribute(SessionConst.SESSION_LOGIN_MEMBER);

        return "redirect:/";
    }
}
