package com.wooyeon.yeon.user.service;

import com.wooyeon.yeon.user.domain.Profile;
import com.wooyeon.yeon.user.domain.User;
import com.wooyeon.yeon.user.dto.LoginDto;
import com.wooyeon.yeon.user.dto.LogoutDto;
import com.wooyeon.yeon.user.dto.auth.TokenDto;
import com.wooyeon.yeon.user.repository.ProfileRepository;
import com.wooyeon.yeon.user.repository.UserRepository;
import com.wooyeon.yeon.user.service.auth.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;

    @Transactional
    public TokenDto login(LoginDto.LoginRequest loginReq) {
        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(loginReq.getEmail(), loginReq.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        TokenDto tokenDto = jwtTokenProvider.generateToken(authentication);

        User user = userRepository.findByEmail(authentication.getName());
        Optional<Profile> existsProfile = profileRepository.findByUser(user);

        if (null == user) {
            throw new IllegalArgumentException("user is not found");
        }

        if (existsProfile.isPresent()) {
            tokenDto.updateStatusCode(HttpStatus.SC_OK); // profile까지 등록한 user
        } else {
            tokenDto.updateStatusCode(3000); // profile 미등록 user
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
