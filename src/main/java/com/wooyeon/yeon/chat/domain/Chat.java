package com.wooyeon.yeon.chat.domain;

import com.wooyeon.yeon.profileChoice.domain.UserMatch;
import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long chatId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userMatchId")
    private UserMatch userMatch;

    @Column
    private String sender;

    @Column(length = 2000)
    private String message;

    @Column
    private LocalDateTime sendTime;
}