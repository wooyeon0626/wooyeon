package com.wooyeon.yeon.user.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailAuth {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 100, nullable = false)
    private String email;

    @Column(length = 36, nullable = false)
    private String authToken;

    @Column(nullable = false)
    private LocalDateTime expireDate;

    private boolean expired;

    @Builder
    public EmailAuth(String email, String authToken, LocalDateTime expireDate, boolean expired) {
        this.email = email;
        this.authToken = authToken;
        this.expireDate = expireDate;
        this.expired = expired;
    }

    public void emailVerifiedSuccess() {
        this.expired = true;
    }
}
