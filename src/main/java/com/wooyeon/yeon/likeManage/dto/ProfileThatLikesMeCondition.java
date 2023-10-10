package com.wooyeon.yeon.likeManage.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class ProfileThatLikesMeCondition {
    private Long myUserid;
    private UUID userCode;
    //private Long likedMeUserCode;
}
