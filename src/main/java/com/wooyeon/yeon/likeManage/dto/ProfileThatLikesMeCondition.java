package com.wooyeon.yeon.likeManage.dto;

import lombok.Data;

@Data
public class ProfileThatLikesMeCondition {
    private Long myUserCode;
    private Long likedMeUserCode;
}
