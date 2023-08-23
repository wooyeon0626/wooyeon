package com.wooyeon.yeon.profileChoice.dto;

import com.wooyeon.yeon.user.domain.Hobby;
import com.wooyeon.yeon.user.domain.Interest;
import com.wooyeon.yeon.user.domain.ProfilePhoto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class RecommandUserDto {
    private Long profileId;
    private char gender;
    private String nickname;
    private String birthday;
    private String locationInfo;
    private String gpsLocationInfo;
    private String mbti;
    private String intro;
    private String userCode;
//    private List<Hobby> hobbys = new ArrayList<>();
//    private List<Interest> interests = new ArrayList<>();
//    private List<ProfilePhoto> profilePhotos = new ArrayList<>();

    public RecommandUserDto(Long profileId, char gender, String nickname, String birthday, String locationInfo, String gpsLocationInfo, String mbti, String intro, String userCode) {
        this.profileId = profileId;
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
