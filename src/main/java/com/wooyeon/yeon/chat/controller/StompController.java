package com.wooyeon.yeon.chat.controller;

import com.wooyeon.yeon.chat.dto.StompDto;
import com.wooyeon.yeon.chat.service.ChatService;
import com.wooyeon.yeon.common.fcm.dto.FcmDto;
import com.wooyeon.yeon.common.fcm.service.FcmService;
import com.wooyeon.yeon.common.security.SecurityService;
import com.wooyeon.yeon.exception.ExceptionCode;
import com.wooyeon.yeon.exception.WooyeonException;
import com.wooyeon.yeon.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequiredArgsConstructor
public class StompController {
    private final SimpMessageSendingOperations simpMessageSendingOperations;
    private final ChatService chatService;
    private final SecurityService securityService;
    private final FcmService fcmService;
    private final UserRepository userRepository;

    private Map<String , String> sessionStore = new ConcurrentHashMap<>();

    /*
        /queue/chat/room/{matchId}    - 채팅방 메시지 URL
        /app/chat/message             - 메시지 발생 이벤트 URL
        /app/unsubscribe              - 구독 취소 URL
    */

    @MessageMapping("/chat/message")
    public void enter(StompDto stompDto) {
        String loginEmail = "young1@naver.com";
        Long roomId = stompDto.getRoomId();

        if (stompDto.getType().equals(StompDto.MessageType.ENTER.toString())) {
            if (!sessionStore.containsKey(roomId.toString()) || "0".equals(sessionStore.get(roomId.toString()))) {
                sessionStore.put(roomId.toString(), "1");
            } else if ("1".equals(sessionStore.get(roomId.toString()))) {
                sessionStore.put(roomId.toString(), "1");
            }
        }

        if (stompDto.getType().equals(StompDto.MessageType.TALK.toString())) {
            simpMessageSendingOperations.convertAndSend("/queue/chat/room/" + stompDto.getRoomId(), stompDto);
        }

        if (stompDto.getType().equals(StompDto.MessageType.QUIT.toString())) {
            String sessionCount = sessionStore.get(roomId.toString());
            int count = Integer.parseInt(sessionCount);
            count -= 1;
            sessionStore.put(roomId.toString(), String.valueOf(count));
        }

        if ("1".equals(sessionStore.get(roomId.toString()))) {
            try {
                fcmService.sendMessageTo(FcmDto.buildRequest(loginEmail, stompDto, userRepository));
            } catch (IOException e) {
                throw new WooyeonException(ExceptionCode.FCM_SEND_FAIL_ERROR);
            }
        }
        chatService.saveChat(stompDto, sessionStore);
    }
}