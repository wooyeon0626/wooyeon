package com.wooyeon.yeon.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ProfileRequestDto {
    private char gender;
    private String nickname;
    private String birthday;
    private String gpsLocationInfo;
    private String mbti;
    private String intro;
    private String hobby;
    private String interest;
//    private boolean faceVerify;
}
