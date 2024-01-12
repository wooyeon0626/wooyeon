package com.wooyeon.yeon.chat.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Block {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long blockId;

    @Column
    private Long toId;

    @Column
    private Long fromId;

    @Column
    private LocalDateTime BlockTime;
}
