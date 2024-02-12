package com.wooyeon.yeon.user.service.auth;

import com.wooyeon.yeon.exception.ExceptionCode;
import com.wooyeon.yeon.exception.WooyeonException;
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
            throw new WooyeonException(ExceptionCode.INVALID_REFRESH_TOKEN);
        }

        Authentication authentication = jwtTokenProvider.getAuthentication(reissueRequestDto.getAccessToken());

        User user = userRepository.findByEmail(authentication.getName());

        if (null == user.getRefreshToken()) {
            throw new WooyeonException(ExceptionCode.NOT_EXIST_REFRESH_TOKEN);
        }

        if (!user.getRefreshToken().equals(reissueRequestDto.getRefreshToken())) {
            throw new WooyeonException(ExceptionCode.NOT_MATCH_REFRESH_TOKEN);
        }

        TokenDto tokenDto = jwtTokenProvider.generateToken(authentication);

        user.updateAccessToken(tokenDto.getAccessToken());

        return ReissueResponseDto.builder()
                .accessToken(tokenDto.getAccessToken())
                .build();
    }
}
