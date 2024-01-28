package com.wooyeon.yeon.chat.dto;


import com.wooyeon.yeon.chat.domain.Chat;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.time.LocalDateTime;

public class RoomDto {

    @Getter
    @Builder
    public static class RoomResponse {
        private Long matchId;
        private String profilePhoto;
        private String name;
        private LocalDateTime lastTime;
        private String lastMessage;
        private boolean pinToTop;
        private Long unReadChatCount;
    }

    @Getter
    public static class SearchRoomRequest {
        @NonNull
        private String SearchWord;
    }

    @Getter
    @Builder
    public static class SearchRoomResponse {
        private Long matchId;
        private String profilePhoto;
        private String name;
    }

    public static RoomResponse updateChatInfo(RoomResponse response, Chat chat) {
        response.lastMessage = chat.getMessage();
        response.lastTime = chat.getSendTime();

        return response;
    }
}
