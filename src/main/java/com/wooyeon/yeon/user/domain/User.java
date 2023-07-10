package com.wooyeon.yeon.user.domain;

import lombok.*;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;

    @Column(length = 11, nullable = false)
    private String phone;

    @Column(nullable = false)
    private char gender;

    @Column(length = 50, nullable = false)
    private String nickname;

    @Column
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "user")
    private List<ProfilePhoto> profilePhotos=new ArrayList<>();

    @Column(length = 8, nullable = false)
    private String birthday;

    @Column(length = 100, nullable = false)
    private String email;

    @Column(length = 100, nullable = false)
    private String locationInfo;

    @Column(length = 100, nullable = false)
    private String gpsLocationInfo;

    @Column(length = 4)
    private String mbti;

    @Column(length = 50)
    private String intro;

    @Column
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "user")
    private List<Hobby> hobbys=new ArrayList<>();

    @Column
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "user")
    private List<Interest> interests=new ArrayList<>();

    @Builder
    public User(Long userId, String phone, char gender, String nickname, List<ProfilePhoto> profilePhotos, String birthday, String email, String locationInfo, String gpsLocationInfo, String mbti, String intro, List<Hobby> hobbys, List<Interest> interests) {
        this.userId=userId;
        this.phone=phone;
        this.gender=gender;
        this.nickname=nickname;
        this.profilePhotos=profilePhotos;
        this.birthday=birthday;
        this.email=email;
        this.locationInfo=locationInfo;
        this.gpsLocationInfo=gpsLocationInfo;
        this.mbti=mbti;
        this.intro=intro;
        this.hobbys=hobbys;
        this.interests=interests;
    }
}