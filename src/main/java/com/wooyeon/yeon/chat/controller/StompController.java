package com.wooyeon.yeon.chat.controller;

import com.wooyeon.yeon.chat.dto.StompDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StompController {
    private final SimpMessageSendingOperations simpMessageSendingOperations;

    /*
        /queue/chat/room/{matchId}    - 구독
        /app/chat/message             - 메시지 발생
    */

    @MessageMapping("/chat/message")
    public void enter(StompDto message) {
        if (StompDto.MessageType.ENTER.equals(message.getType())) {
            message.setMessage(message.getSender()+"님이 입장하였습니다.");
        }
        simpMessageSendingOperations.convertAndSend("/queue/chat/room/"+message.getRoomId(),message);
    }
}