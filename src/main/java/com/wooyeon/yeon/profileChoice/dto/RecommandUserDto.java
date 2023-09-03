package com.wooyeon.yeon.profileChoice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class RecommandUserDto {
//    private Long profileId;
    private char gender;
    private String nickname;
    private String birthday;
    private String locationInfo;
    private String gpsLocationInfo;
    private String mbti;
    private String intro;
    private UUID userCode;
//    private List<Hobby> hobbys = new ArrayList<>();
//    private List<Interest> interests = new ArrayList<>();
//    private List<ProfilePhoto> profilePhotos = new ArrayList<>();

    public RecommandUserDto(char gender, String nickname, String birthday, String locationInfo, String gpsLocationInfo, String mbti, String intro, UUID userCode) {
//        this.profileId = profileId;
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
