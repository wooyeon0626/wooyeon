package com.wooyeon.yeon.user.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "USER_ROLES")
public class UserRoles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_user_id", referencedColumnName = "userId")
    private User user;

    @Column(name = "roles")
    private String roles;

//    public void updateUserId(User id) {this.userUserId = id; }
    public void updateRoles(String roles) { this.roles = roles; }
}
