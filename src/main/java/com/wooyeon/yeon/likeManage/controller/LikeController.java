package com.wooyeon.yeon.likeManage.controller;

import com.wooyeon.yeon.likeManage.domain.UserLike;
import com.wooyeon.yeon.likeManage.dto.*;
import com.wooyeon.yeon.likeManage.service.LikeService;
import com.wooyeon.yeon.user.domain.Profile;
import com.wooyeon.yeon.user.domain.User;
import com.wooyeon.yeon.user.dto.UserDto;
import com.wooyeon.yeon.user.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author heesoo
 */
@RestController
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;
    private final UserService userService;

    /**
     * 본인과 좋아요 한 userId를 입력받아서 like합니다.
     *
     * @param dto
     * @return 성공시 true
     * @deprecated
     */
    @PostMapping("/like/user/fromUserid")
    public boolean doLike(@RequestBody LikeDto dto) {
        User likeFromUser = userService.findByUserId(dto.getLikeFromUserId());
        User likeToUser = userService.findByUserId(dto.getLikeToUserId());
        likeService.saveUserLike(likeFromUser, likeToUser);
        return true;
    }

    //userCode(UUID)로 좋아요

    /**
     * 본인과 좋아요 한 유저들의 userCode(UUID)를 입력받아서 like합니다.
     *
     * @param dto
     * @return response : responseMessage 포함된 객체
     */
    @PostMapping("/like/user")
    public CreateLikeResponse doLike(@RequestBody RequestLikeRequestDto dto) {
        User likeFromUser = userService.findByUserUUID(dto.getLikeFromUserUUID());
        User likeToUser = userService.findByUserUUID(dto.getLikeToUserUUID());
        CreateLikeResponse response = likeService.saveUserLike(likeFromUser, likeToUser);
        //매치 되었는지 확인
        boolean isMatch = likeService.checkMatch(likeFromUser.getUserId(), likeToUser.getUserId());
        return response;
    }

    @GetMapping("/like/from")
    public ResponseLikeMe findLikeMe(@RequestBody RequestLikeMeDto dto) {
        List<Profile> profileList = likeService.findLikeForMeProfileList(dto.getMyUserCode());
        return null;
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