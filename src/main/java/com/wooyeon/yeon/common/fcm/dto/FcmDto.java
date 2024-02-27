package com.wooyeon.yeon.common.fcm.dto;

import com.wooyeon.yeon.chat.dto.StompDto;
import com.wooyeon.yeon.exception.ExceptionCode;
import com.wooyeon.yeon.profileChoice.domain.UserMatch;
import com.wooyeon.yeon.profileChoice.repository.MatchRepository;
import com.wooyeon.yeon.user.domain.User;
import com.wooyeon.yeon.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FcmDto {
    private boolean validateOnly;
    private Message message;

    @Builder
    @AllArgsConstructor
    @Getter
    public static class Message {
        private Notification notification;
        private String token;
        private Data data;
    }

    @Builder
    @AllArgsConstructor
    @Getter
    public static class Notification {
        private String title;
        private String body;
    }

    @Builder
    @AllArgsConstructor
    @Getter
    public static class Data {
        private String name;
        private String description;
    }

    @Getter
    @Builder
    public static class Request {
        // 해당 기기 토큰
        String targetToken;
        String title;
        String body;
        String email;
        String description;
    }

    @Getter
    @NoArgsConstructor
    public static class SaveRequest {
        @NotNull
        private String fcmToken;
    }

    @Getter
    @Builder
    public static class SaveResponse {
        private HttpStatus status;
    }

    public static Request buildRequest(String loginEmail , StompDto stompDto, UserRepository userRepository,
                                       MatchRepository matchRepository) {

        User user = userRepository.findOptionalByEmail(loginEmail)
                .orElseThrow(() -> new IllegalArgumentException(ExceptionCode.LOGIN_USER_NOT_FOUND.toString()));

        List<UserMatch> userMatchList = matchRepository.findAllByUser1OrUser2(user, user);

        String matchUserId = userMatchList.get(0).getUser2().getUserEmail();

        if (0 < userMatchList.size()) {
            if (!loginEmail.equals(userMatchList.get(0).getUser1().getUserEmail())) {
                matchUserId = userMatchList.get(0).getUser1().getUserEmail();
            }
        }

        User matchUser = userRepository.findOptionalByEmail(matchUserId)
                .orElseThrow(() -> new IllegalArgumentException(ExceptionCode.LOGIN_USER_NOT_FOUND.toString()));

        return FcmDto.Request.builder()
                .title(user.getUserProfile().getNickname())
                .body(stompDto.getMessage())
                .targetToken(matchUser.getFcmToken())
                .email(loginEmail)
                .build();
    }
}
