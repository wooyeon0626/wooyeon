package com.wooyeon.yeon.chat.dto;


import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

public class RoomDto {

    public class RoomRequest {
    }

    @Getter
    @Builder
    public static class RoomResponse {
        private Long matchId;
        private Long profilePhoto;
        private String name;
        private LocalDateTime lastTime;
        private String lastMessage;
    }

    @Getter
    @Builder
    public static class SearchRoomResponse {
        private Long matchId;
        private Long profilePhoto;
        private String name;
    }
}
