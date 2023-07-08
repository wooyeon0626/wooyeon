package com.wooyeon.yeon.profileChoice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchDto {
    private Long matchId;
    private Long likeId1;
    private Long likeId2;
    private boolean isMatch;
    private Timestamp generateTime;
}
