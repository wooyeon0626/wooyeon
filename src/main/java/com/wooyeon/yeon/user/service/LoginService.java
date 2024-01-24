package com.wooyeon.yeon.user.service;

import com.wooyeon.yeon.user.domain.User;
import com.wooyeon.yeon.user.dto.LoginDto;
import com.wooyeon.yeon.user.dto.LogoutDto;
import com.wooyeon.yeon.user.dto.auth.TokenDto;
import com.wooyeon.yeon.user.repository.UserRepository;
import com.wooyeon.yeon.user.service.auth.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @Transactional
    public TokenDto login(LoginDto.LoginRequest loginReq) {
        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(loginReq.getEmail(), loginReq.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        TokenDto tokenDto = jwtTokenProvider.generateToken(authentication);

        User user = userRepository.findByEmail(authentication.getName());

        if (null == user) {
            throw new IllegalArgumentException("user is not found");
        }

        user.updateAccessToken(tokenDto.getAccessToken());
        user.updateRefreshToken(tokenDto.getRefreshToken());

        return tokenDto;
    }

    @Transactional
    public LogoutDto.LogoutResponse logout(LogoutDto.LogoutRequest logoutRequest) {
        Authentication authentication = jwtTokenProvider.getAuthentication(logoutRequest.getAccessToken());

        User user = userRepository.findByEmail(authentication.getName());

        if (null == user) {
            throw new IllegalArgumentException("user is not found");
        }
        user.updateAccessToken(null);
        user.updateRefreshToken(null);

        return LogoutDto.LogoutResponse.builder()
                .status("OK")
                .build();
    }
}
