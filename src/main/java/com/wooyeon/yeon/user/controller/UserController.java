package com.wooyeon.yeon.user.controller;

import com.wooyeon.yeon.user.dto.MemberRegisterRequestDto;
import com.wooyeon.yeon.user.dto.MemberRegisterResponseDto;
import com.wooyeon.yeon.user.service.EmailAuthService;
import com.wooyeon.yeon.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
//@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final EmailAuthService emailAuthService;

    @Autowired
    public UserController(UserService userService, EmailAuthService emailAuthService) {
        this.userService = userService;
        this.emailAuthService = emailAuthService;
    }

    @GetMapping("/register")
    public ResponseEntity<MemberRegisterResponseDto> registerMember(String email, String password) {
        MemberRegisterRequestDto requestDto = new MemberRegisterRequestDto();
        requestDto.setEmail(email);
        requestDto.setPassword(password);
        MemberRegisterResponseDto responseDto = userService.registerMember(requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestParam String email, String token) {
        emailAuthService.verifyEmail(email, token);
        return ResponseEntity.ok("이메일 인증이 완료되었습니다.");
    }
}