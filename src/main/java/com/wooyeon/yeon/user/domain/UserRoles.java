package com.wooyeon.yeon.user.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "user_roles")
public class UserRoles {

    @Id
    @GeneratedValue
    @Column(name = "user_user_id")
    private Long userUserId;

    @Column
    private String roles;

    @OneToOne
    @JoinColumn(name = "user_user_id", referencedColumnName = "user_id")
    private User user;
}
