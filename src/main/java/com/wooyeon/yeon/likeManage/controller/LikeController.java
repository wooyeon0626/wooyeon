package com.wooyeon.yeon.likeManage.controller;

import com.wooyeon.yeon.likeManage.domain.UserLike;
import com.wooyeon.yeon.likeManage.dto.LikeDto;
import com.wooyeon.yeon.likeManage.service.LikeService;
import com.wooyeon.yeon.user.domain.User;
import com.wooyeon.yeon.user.dto.UserDto;
import com.wooyeon.yeon.user.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;
    private final UserService userService;

    @PostMapping("/like/user")
    public boolean doLike(@RequestBody LikeDto dto) {
        User likeFromUser = userService.findByUserId(dto.getLikeFromUserId());
        User likeToUser = userService.findByUserId(dto.getLikeToUserId());
        likeService.saveUserLike(likeFromUser, likeToUser);
        return true;
    }

    // 좋아요 이후 응답할 정보
    @Data
    @AllArgsConstructor
    static class CreateLikeResponse {
        private Long likeId;
        private String likedFromUserName; // 좋아요 누른 본인 이름
        private String likedToUserName;// 좋아요 받은 상대 이름
    }
}