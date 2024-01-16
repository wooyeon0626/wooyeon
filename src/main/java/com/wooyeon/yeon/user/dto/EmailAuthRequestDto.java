package com.wooyeon.yeon.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class EmailAuthRequestDto {

//    private String email;
//    private String authToken;
    private String auth;
}
