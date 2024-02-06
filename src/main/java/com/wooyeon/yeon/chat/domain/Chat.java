package com.wooyeon.yeon.chat.domain;

import com.wooyeon.yeon.profileChoice.domain.UserMatch;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long chatId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_match_id")
    private UserMatch userMatch;

    @Column
    private String sender;

    @Column(length = 2000)
    private String message;

    @Column(name = "is_checked")
    private boolean isChecked;

    @Column(name = "send_time")
    private LocalDateTime sendTime;
}