package com.wooyeon.yeon.profileChoice.controller;

import com.wooyeon.yeon.user.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InitUser {
    private final InitMemberService initMemberService;

/*    @PostConstruct
    public void init() {
        initMemberService.init();
    }*/

    @Component
    static class InitMemberService {
        @PersistenceContext
        private EntityManager em;

        /*@Transactional
        public void init() {
            for (int i = 0; i < 100; i++) {
                em.persist(Profile.builder()
                        .profilePhotos(null)
                        .birthday("0101")
                        .intro("자기소개샘플")
                        .mbti("MBTI")
                        .gpsLocationInfo("3km")
                        .nickname("닉네임" + i)
                        .hobbies(null)
                        .interests(null)
                        .locationInfo("주소" + i)
                        .gender('M')
                        .build());
            }
        }*/
    }
}
