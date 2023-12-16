package com.wooyeon.yeon.user.controller;

import com.wooyeon.yeon.user.dto.LoginDto;
import com.wooyeon.yeon.user.dto.LogoutDto;
import com.wooyeon.yeon.user.dto.auth.TokenDto;
import com.wooyeon.yeon.user.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody LoginDto.LoginRequest loginRequest) {
        return ResponseEntity.ok(loginService.login(loginRequest));
    }

    @PostMapping("/logout")
    public ResponseEntity<LogoutDto.LogoutResponse> logout(@RequestBody LogoutDto.LogoutRequest logoutRequest) {
        return ResponseEntity.ok(loginService.logout(logoutRequest));
    }
}
