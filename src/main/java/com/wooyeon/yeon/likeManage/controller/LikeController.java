package com.wooyeon.yeon.likeManage.controller;

import com.wooyeon.yeon.likeManage.domain.UserLike;
import com.wooyeon.yeon.likeManage.service.LikeService;
import com.wooyeon.yeon.user.domain.User;
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


//    @PostMapping("/{userName}/like/{liked_user_id}") // 경로는 수정 예정. 지금은 예시, /본인이름/like/좋아요한상대아이디
//    public CreateLikeResponse likeMember(@PathVariable("liked_user_id") Long likedUserId,
//                                         @RequestBody @Valid UserRequest request,/* 클라쪽에서 json으로 본인의 User 정보 줌. (id만, 수정가능)*/
//                                         @PathVariable String userName /*본인 이름, 일단 사용 안함.*/) {
//
//        Long userId = request.getUserId();
//        User likeFromUser = userService.findUserById(userId);
//        User likeToUser = userService.findUserById(likedUserId);
//        UserLike like = likeService.createLike(likeFromUser, likeToUser);
//
//        return new CreateLikeResponse(like.getLikeId(), likeFromUser.getNickname(), likeToUser.getNickname());
//    }

    // 클라로 부터 전달 받을 유저 dto
    @Data
    @AllArgsConstructor
    static class UserRequest {
        @NotNull
        private Long userId;
    }

    // 클라에게 응답할 정보 dto
    @Data
    @AllArgsConstructor
    static class CreateLikeResponse {
        private Long likeId;
        private String likedFromUserName; // 좋아요 누른 본인 이름
        private String likedToUserName;// 좋아요 받은 상대 이름
    }
}
