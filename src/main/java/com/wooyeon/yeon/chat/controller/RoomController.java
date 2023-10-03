package com.wooyeon.yeon.chat.controller;

import com.wooyeon.yeon.chat.dto.RoomDto;
import com.wooyeon.yeon.chat.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/room")
public class RoomController {

    private final RoomService roomService;

    @PostMapping("/list")
    public List<RoomDto.RoomResponse> findMatchRoomList() {
        return roomService.matchRoomList();
    }

    @GetMapping("/list")
    public Set<RoomDto.SearchRoomResponse> searchMatchRoomList(@RequestParam String searchWord) {
        return roomService.searchMatchRoomList(searchWord);
    }
}
