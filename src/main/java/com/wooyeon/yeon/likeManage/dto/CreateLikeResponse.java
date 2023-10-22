package com.wooyeon.yeon.likeManage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author heesoo
 */
@Data
@AllArgsConstructor
public class CreateLikeResponse {
    //좋아요 이후 응답할 정보
    private String responseMessage;
    private String userLikeDeleteMessage;
}