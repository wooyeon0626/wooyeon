package com.wooyeon.yeon.chat.controller;

import com.wooyeon.yeon.chat.dto.RoomDto;
import com.wooyeon.yeon.chat.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/room")
public class RoomController {

    private final RoomService roomService;

    @GetMapping("/list")
    public List<RoomDto.RoomResponse> findMatchRoomList() {
        return roomService.matchRoomList();
    }

    @GetMapping("/search/list")
    public Set<RoomDto.SearchRoomResponse> searchMatchRoomList(@Valid @RequestBody RoomDto.SearchRoomRequest request) {
        return roomService.searchMatchRoomList(request);
    }
}
