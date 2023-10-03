package com.wooyeon.yeon.chat.controller;

import com.wooyeon.yeon.chat.dto.StompDto;
import com.wooyeon.yeon.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StompController {
    private final SimpMessageSendingOperations simpMessageSendingOperations;
    private final ChatService chatService;

    /*
        /queue/chat/room/{matchId}    - 구독
        /app/chat/message             - 메시지 발생
    */

    @MessageMapping("/chat/message")
    public void enter(StompDto stompDto) {
        chatService.saveChat(stompDto);
        simpMessageSendingOperations.convertAndSend("/queue/chat/room/" + stompDto.getRoomId(), stompDto);
    }
}