package com.wooyeon.yeon.chat.controller;

import com.wooyeon.yeon.chat.dto.StompDto;
import com.wooyeon.yeon.chat.service.ChatService;
import com.wooyeon.yeon.common.fcm.dto.FcmDto;
import com.wooyeon.yeon.common.fcm.service.FcmService;
import com.wooyeon.yeon.exception.ExceptionCode;
import com.wooyeon.yeon.exception.WooyeonException;
import com.wooyeon.yeon.profileChoice.repository.MatchRepository;
import com.wooyeon.yeon.user.domain.User;
import com.wooyeon.yeon.user.repository.UserRepository;
import com.wooyeon.yeon.user.service.auth.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequiredArgsConstructor
public class StompController {
    private final SimpMessageSendingOperations simpMessageSendingOperations;
    private final ChatService chatService;
    private final FcmService fcmService;
    private final UserRepository userRepository;
    private final MatchRepository matchRepository;
    private final JwtTokenProvider jwtTokenProvider;

    private static Map<String, String> sessionStore = new ConcurrentHashMap<>();

    /*
        /queue/chat/room/{matchId}    - 채팅방 메시지 URL
        /app/chat/message             - 메시지 발생 이벤트 URL
        /app/unsubscribe              - 구독 취소 URL
    */

    @MessageMapping("/chat/message")
    public void enter(StompDto stompDto, @Header("Authorization") String token) {
        Authentication authentication = jwtTokenProvider.getAuthentication(token.substring(7));
        String loginEmail = authentication.getName();
        Long roomId = stompDto.getRoomId();

        if (stompDto.getType().equals(StompDto.MessageType.ENTER.toString())) {
            if (!sessionStore.containsKey(roomId.toString()) || "0".equals(sessionStore.get(roomId.toString()))) {
                sessionStore.put(roomId.toString(), "1");
            } else if ("1".equals(sessionStore.get(roomId.toString()))) {
                sessionStore.put(roomId.toString(), "2");
            }
        }

        if (stompDto.getType().equals(StompDto.MessageType.TALK.toString())) {

            User loginUser = userRepository.findOptionalByEmail(loginEmail)
                    .orElseThrow(() -> new WooyeonException(ExceptionCode.LOGIN_USER_NOT_FOUND));

            simpMessageSendingOperations.convertAndSend("/queue/chat/room/" + stompDto.getRoomId(),
                    StompDto.StompRes.builder()
                            .message(stompDto.getMessage())
                            .sendTime(LocalDateTime.now())
                            .senderToken(loginUser.getAccessToken())
                            .build());

            chatService.saveChat(stompDto, sessionStore, loginEmail);
        }

        if (stompDto.getType().equals(StompDto.MessageType.QUIT.toString())) {
            String sessionCount = sessionStore.get(roomId.toString());
            int count = Integer.parseInt(sessionCount);
            count -= 1;
            sessionStore.put(roomId.toString(), String.valueOf(count));
        }

        if (stompDto.getType().equals(StompDto.MessageType.TALK.toString()) &&
                "1".equals(sessionStore.get(roomId.toString()))) {
            try {
                fcmService.sendMessageTo(FcmDto.buildRequest(loginEmail, stompDto, userRepository, matchRepository));
            } catch (IOException e) {
                throw new WooyeonException(ExceptionCode.FCM_SEND_FAIL_ERROR);
            }
            chatService.saveChat(stompDto, sessionStore, loginEmail);
        }
    }
}