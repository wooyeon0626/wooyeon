package com.wooyeon.yeon.user.service.auth;

import com.wooyeon.yeon.user.domain.User;
import com.wooyeon.yeon.user.dto.ReissueResponseDto;
import com.wooyeon.yeon.user.dto.auth.TokenDto;
import com.wooyeon.yeon.user.dto.ReissueRequestDto;
import com.wooyeon.yeon.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public ReissueResponseDto reissueToken(ReissueRequestDto reissueRequestDto) {
        if(!jwtTokenProvider.validateToken(reissueRequestDto.getRefreshToken())) {
            throw new RuntimeException("Refresh Token이 유효하지 않습니다.");
        }

        Authentication authentication = jwtTokenProvider.getAuthentication(reissueRequestDto.getAccessToken());

        User user = userRepository.findByEmail(authentication.getName());

        if (null == user.getRefreshToken()) {
            throw new RuntimeException("로그아웃 된 유저입니다.");
        }

        if (!user.getRefreshToken().equals(reissueRequestDto.getRefreshToken())) {
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
        }

        TokenDto tokenDto = jwtTokenProvider.generateToken(authentication);

        user.updateAccessToken(tokenDto.getAccessToken());

        return ReissueResponseDto.builder()
                .accessToken(tokenDto.getAccessToken())
                .build();
    }
}
