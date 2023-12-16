package com.wooyeon.yeon.likeManage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class ResponseProfileDto {
    private char gender;
    private String nickname;
    private String birthday;
    private String gpsLocationInfo;
    private String mbti;
    private String intro;
}
