package com.wooyeon.yeon.profileChoice.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wooyeon.yeon.profileChoice.dto.RecommandUserCondition;
import com.wooyeon.yeon.profileChoice.dto.RecommandUserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.List;

import static com.wooyeon.yeon.user.domain.QProfile.profile;

public class MatchRepositoryImpl implements MatchRepositoryRecommandUserList {

    private final JPAQueryFactory queryFactory;

    public MatchRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<RecommandUserDto> searchUserProfileSimple(RecommandUserCondition condition, Pageable pageable) {
        QueryResults<RecommandUserDto> results = queryFactory
                .select(Projections.bean(RecommandUserDto.class,
                        profile.id.as("profileId"),
                        profile.gender,
                        profile.nickname,
                        profile.birthday,
                        profile.locationInfo,
                        profile.gpsLocationInfo,
                        profile.mbti,
                        profile.intro,
                        profile.user.userCode
//                        profile.hobbys,
//                        profile.interests,
//                        profile.profilePhotos
                ))
                .from(profile)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<RecommandUserDto> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content, pageable, total);
    }
}
