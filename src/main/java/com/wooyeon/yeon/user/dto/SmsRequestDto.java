package com.wooyeon.yeon.user.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class SmsRequestDto {
    private String type;
    private String contentType;
    private String countryCode;
    private String from;
    private String content;
    private List<SmsDto> messages;
}
