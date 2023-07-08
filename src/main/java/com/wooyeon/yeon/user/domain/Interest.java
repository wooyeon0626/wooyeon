package com.wooyeon.yeon.user.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Interest {
    @Id
    private Long interestId;
    private String interest;
}
