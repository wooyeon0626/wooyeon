package com.wooyeon.yeon.user.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Hobby {
    @Id
    private Long hobbyId;

    private String hobby;
}
