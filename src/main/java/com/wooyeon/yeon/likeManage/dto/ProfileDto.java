package com.wooyeon.yeon.likeManage.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class ProfileDto {
    private char gender;
    private String nickname;
    private String birthday;
    private String locationInfo;
    private String gpsLocationInfo;
    private String mbti;
    private String intro;
    private UUID userCode;


    public ProfileDto(char gender, String nickname, String birthday, String locationInfo, String gpsLocationInfo, String mbti, String intro, UUID userCode) {
        this.gender = gender;
        this.nickname = nickname;
        this.birthday = birthday;
        this.locationInfo = locationInfo;
        this.gpsLocationInfo = gpsLocationInfo;
        this.mbti = mbti;
        this.intro = intro;
        this.userCode = userCode;
    }
}
