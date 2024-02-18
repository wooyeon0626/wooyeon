package com.wooyeon.yeon.user.service;

import com.wooyeon.yeon.exception.ExceptionCode;
import com.wooyeon.yeon.exception.WooyeonException;
import com.wooyeon.yeon.user.domain.Profile;
import com.wooyeon.yeon.user.domain.User;
import com.wooyeon.yeon.user.dto.ExpiredCheckDto;
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
import org.springframework.web.bind.annotation.PostMapping;

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
            throw new WooyeonException(ExceptionCode.USER_NOT_FOUND);
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
            throw new WooyeonException(ExceptionCode.USER_NOT_FOUND);
        }
        user.updateAccessToken(null);
        user.updateRefreshToken(null);

        return LogoutDto.LogoutResponse.builder()
                .status("OK")
                .build();
    }

    public ExpiredCheckDto.ExpiredCheckResponse checkTokenAndProfile(String accessToken) {

        boolean expiredCheckAccessToken = jwtTokenProvider.validateToken(accessToken);

        if(!expiredCheckAccessToken) {
            throw new WooyeonException(ExceptionCode.EXPIRED_JWT_TOKEN);
        }

        if(expiredCheckAccessToken) {
            User user = userRepository.findByAccessToken(accessToken);
            Optional<Profile> existsProfile = profileRepository.findByUser(user);

            if (existsProfile.isPresent()) {    // profile까지 등록한 user
                return ExpiredCheckDto.ExpiredCheckResponse.builder()
                        .existsProfile(HttpStatus.SC_OK)
                        .build();
            } else {     // profile 미등록 user
                return ExpiredCheckDto.ExpiredCheckResponse.builder()
                        .existsProfile(3000)
                        .build();
            }
        }
        return ExpiredCheckDto.ExpiredCheckResponse.builder()
                .existsProfile(4004)
                .build();
    }
}
