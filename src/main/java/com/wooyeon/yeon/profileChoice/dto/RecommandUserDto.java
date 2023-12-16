package com.wooyeon.yeon.profileChoice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

//RecommandProfileDto로 수정
@Data
@AllArgsConstructor
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
}
