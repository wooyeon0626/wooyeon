package com.wooyeon.yeon.likeManage.dto;

import lombok.Data;

@Data
public class LikeDto {
    private Long likeId;
    private Long likeToUserId;
    private Long likeFromUserId;
}
