package com.wooyeon.yeon.likeManage.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResponseProfileDto {
    private char gender;
    private String nickname;
    private String birthday;
    private String gpsLocationInfo;
    private String mbti;
    private String intro;

    public ResponseProfileDto(char gender, String nickname, String birthday, String gpsLocationInfo, String mbti, String intro) {
        this.gender = gender;
        this.nickname = nickname;
        this.birthday = birthday;
        this.gpsLocationInfo = gpsLocationInfo;
        this.mbti = mbti;
        this.intro = intro;
    }
}
