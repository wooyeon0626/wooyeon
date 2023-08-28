package com.wooyeon.yeon.likeManage.controller;

import com.wooyeon.yeon.likeManage.domain.UserLike;
import com.wooyeon.yeon.likeManage.dto.CreateLikeResponse;
import com.wooyeon.yeon.likeManage.dto.LikeDto;
import com.wooyeon.yeon.likeManage.dto.RequestLikeRequestDto;
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

    //userId로 좋아요
    @PostMapping("/like/user/fromUserid")
    public boolean doLike(@RequestBody LikeDto dto) {
        User likeFromUser = userService.findByUserId(dto.getLikeFromUserId());
        User likeToUser = userService.findByUserId(dto.getLikeToUserId());
        likeService.saveUserLike(likeFromUser, likeToUser);
        return true;
    }

    //userCode(UUID)로 좋아요
    @PostMapping("/like/user")
    public CreateLikeResponse doLike(@RequestBody RequestLikeRequestDto dto) {
        User likeFromUser = userService.findByUserUUID(dto.getLikeFromUserUUID());
        User likeToUser = userService.findByUserUUID(dto.getLikeToUserUUID());
        CreateLikeResponse response = likeService.saveUserLike(likeFromUser, likeToUser);
        //매치 되었는지 확인
        boolean isMatch = likeService.checkMatch(likeFromUser.getUserId(), likeToUser.getUserId());
        return response;
    }

//    // 좋아요 이후 응답할 정보
//    @Data
//    @AllArgsConstructor
//    static class CreateLikeResponse {
//        private Long likeId;
//        private String likedFromUserName; // 좋아요 누른 본인 이름
//        private String likedToUserName;// 좋아요 받은 상대 이름
//    }
}