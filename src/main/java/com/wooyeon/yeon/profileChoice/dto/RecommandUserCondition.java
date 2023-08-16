package com.wooyeon.yeon.profileChoice.dto;

import lombok.Data;

// 추천 유저 리스트 검색 조건(나이 성별같은 것 으로 필터링하여 조회)
@Data
public class RecommandUserCondition {
    private String gender;
    private String gpsLocationInfo;
    private Integer ageGoe;//나이가 크거가 같거나 , null 들어올 수 있어서 Integer 사용
    private Integer ageLoe;//나이가 작거나 같거나
}
