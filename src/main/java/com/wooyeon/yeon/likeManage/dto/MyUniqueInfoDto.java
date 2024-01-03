package com.wooyeon.yeon.likeManage.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class MyUniqueInfoDto {
    private Long myUserid;
    private UUID userCode;
}
