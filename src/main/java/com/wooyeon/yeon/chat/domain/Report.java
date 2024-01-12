package com.wooyeon.yeon.chat.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long reportId;

    @Column
    private Long reportUser;

    @Column
    private int count;

    @Column
    private LocalDateTime updateTime;
}
