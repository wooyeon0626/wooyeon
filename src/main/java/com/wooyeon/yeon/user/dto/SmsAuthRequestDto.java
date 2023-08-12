package com.wooyeon.yeon.user.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@Getter
@Builder
public class SmsAuthRequestDto {
    private String type;
    private String contentType;
    private String countryCode;
    private String from;
    private String content;
    private List<SmsDto> messages;
}
