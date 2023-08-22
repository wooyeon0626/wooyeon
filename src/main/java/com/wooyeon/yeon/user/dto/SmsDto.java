package com.wooyeon.yeon.user.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SmsDto {
    private String to;
//    private String content;


    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}