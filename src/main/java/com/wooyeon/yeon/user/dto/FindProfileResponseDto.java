package com.wooyeon.yeon.user.dto;

import com.wooyeon.yeon.user.domain.ProfilePhoto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Builder
@AllArgsConstructor
@Getter
public class FindProfileResponseDto {
    private char gender;
    private String nickname;
    private String birthday;
    private String locationInfo;
    private String gpsLocationInfo;
    private String mbti;
    private String intro;
    private String hobby;
    private String interest;
    private List<String> profilePhotos;
}
