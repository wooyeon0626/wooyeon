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
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class StompController {
    private final SimpMessageSendingOperations simpMessageSendingOperations;
    private final ChatService chatService;
    private final SecurityService securityService;
    private final FcmService fcmService;
    private final UserRepository userRepository;


    /*
        /queue/chat/room/{matchId}    - 채팅방 메시지 URL
        /app/chat/message             - 메시지 발생 이벤트 URL
        /app/unsubscribe              - 구독 취소 URL
    */

    @MessageMapping("/chat/message")
    public void enter(StompDto stompDto, WebSocketSession session, StompHeaderAccessor accessor) {
        String loginEmail = securityService.getCurrentUserEmail();
        session.getAttributes().put(loginEmail, accessor.getUser().getName());
        simpMessageSendingOperations.convertAndSend("/queue/chat/room/" + stompDto.getRoomId(), stompDto);

        int sessionCount = chatService.calculateUserCount();

        if(1 == sessionCount) {
            try {
                fcmService.sendMessageTo(FcmDto.buildRequest(loginEmail, stompDto, userRepository));
            } catch (IOException e) {
                throw new WooyeonException(ExceptionCode.FCM_SEND_FAIL_ERROR);
            }
        }

        chatService.saveChat(stompDto);
    }

    @MessageMapping("/unsubscribe")
    public void handleUnsubscription(WebSocketSession session) {
        session.getAttributes().remove(securityService.getCurrentUserEmail());
    }
}