package com.wooyeon.yeon.chat.dto;


import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

public class RoomDto {

    @Getter
    @Builder
    public static class RoomResponse {
        private Long matchId;
        private Long profilePhoto;
        private String name;
        private LocalDateTime lastTime;
        private String lastMessage;
        private boolean pinToTop;
        private int unReadChatCount;
    }

    @Getter
    public static class SearchRoomRequest {
        private String SearchWord;
    }

    @Getter
    @Builder
    public static class SearchRoomResponse {
        private Long matchId;
        private Long profilePhoto;
        private String name;
    }
}
