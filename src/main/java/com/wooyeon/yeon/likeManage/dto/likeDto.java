package com.wooyeon.yeon.likeManage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class likeDto {
    private Long likeId;
    private Long likeToUserId;
    private Long likeFromUserId;
}
