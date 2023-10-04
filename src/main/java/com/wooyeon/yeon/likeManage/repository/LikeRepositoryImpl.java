package com.wooyeon.yeon.likeManage.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wooyeon.yeon.likeManage.domain.QUserLike;
import com.wooyeon.yeon.likeManage.dto.ProfileDto;
import com.wooyeon.yeon.likeManage.dto.ProfileThatLikesMeCondition;
import com.wooyeon.yeon.likeManage.dto.ResponseLikeMe;
import com.wooyeon.yeon.user.domain.Profile;
import com.wooyeon.yeon.user.domain.QProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.List;

import static com.wooyeon.yeon.user.domain.QProfile.profile;

public class LikeRepositoryImpl implements LikeRepositoryFindProfileList {

    private final JPAQueryFactory queryFactory;

    public LikeRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<ResponseLikeMe> findProfilesWhoLikedMe(ProfileThatLikesMeCondition condition, Pageable pageable) {

        QUserLike userLike = QUserLike.userLike;
        QProfile profile = QProfile.profile;

        // QueryDSL을 사용하여 "나를 좋아요" 한 프로필 리스트 조회
        QueryResults<ResponseLikeMe> results = queryFactory
                .select(Projections.bean(ResponseLikeMe.class,
                        profile.gender,
                        profile.nickname,
                        profile.birthday,
                        profile.gpsLocationInfo,
                        profile.mbti,
                        profile.intro,
                        profile.user.userCode
                ))
                .from(userLike)
                .where(userLike.likeFrom.userId.eq(condition.getMyUserid()))
                .fetchResults();

        List<ResponseLikeMe> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content, pageable, total);

        //return query.fetch();
        //return null;
    }
}
