package com.wooyeon.yeon.user.controller;

import com.wooyeon.yeon.user.dto.auth.ReissueRequestDto;
import com.wooyeon.yeon.user.dto.auth.ReissueResponseDto;
import com.wooyeon.yeon.user.service.auth.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class TokenController {

    private final TokenService tokenService;

    @PostMapping("/reissue-token")
    public ResponseEntity<ReissueResponseDto> reissueToken(@RequestBody ReissueRequestDto reissueRequestDto) throws Exception {
        return ResponseEntity.ok(tokenService.reissueToken(reissueRequestDto));
    }
}
