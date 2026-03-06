package com.complaint.control;

import com.complaint.Dto.LoginDto;
import com.complaint.Dto.UserDto;
import com.complaint.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    @Autowired
    private PasswordEncoder passwordEncoder; // 암호화 객체

    @Autowired
    private UserService userService; // 회원 관련 작업 클래스

    // 로그인 실패시 제공페이지
    @GetMapping("/signIn/error")
    public String loginFail(Model model) {
        model.addAttribute("loginError", "아이디 또는 비밀번호가 잘못되었습니다.");
        return "member/signIn";
    }

    // 로그인 페이지 요청
    @GetMapping("/signIn")
    public String signIn( Model model ) {
        model.addAttribute("loginDto", new LoginDto());
        return "member/signIn";
    }


    //회원가입 작성
    @PostMapping("/signUp")
    public String signUp(@Valid UserDto userDto,
                         BindingResult bindingResult, Model model) {
        // 유효성 검사 실패 : 검사를 통과 못함
        if (bindingResult.hasErrors()) {
            return "member/signUp";
            // 유효하지 않은 값이 있으니 다시 회원 가입 페이지 돌려보내기
        }

        // 회원가입 데이터 저장 실행
        try {
            userService.save(userDto, passwordEncoder);
        } catch(IllegalStateException e) { // 아이디 중복체크시 중복이면 예외
            model.addAttribute("errorMsg", e.getMessage());
            return "member/signUp";
        }
            return "redirect:/"; // 회원가입 저장되면 첫 페이지 이동
        }

    @GetMapping("/signUp")
    public String signUp( Model model) {
        model.addAttribute("userDto",new UserDto());
        return "member/signUp";
    }
}
