package com.wooyeon.yeon.chat.controller;

import com.wooyeon.yeon.chat.dto.ChatDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatController {
    private final SimpMessageSendingOperations simpMessageSendingOperations;

    /*
        /queue/chat/room/{matchId}    - 구독
        /app/chat                     - 메시지 발생
    */

    /**
     * @param chatDto
     * @return 매치 아이디에 따른 구독 채팅방으로 chatDto 값 전송
     */
    @MessageMapping("/chat")
    public void enter(ChatDto chatDto) {
        if(ChatDto.MessageType.ENTER.equals(chatDto.getType())) {
            chatDto.setMessage(chatDto.getSender() + "이 입장했습니다.");
        }
        simpMessageSendingOperations.convertAndSend("/queue/chat/room/" + chatDto.getMatchId(), chatDto);
    }
}