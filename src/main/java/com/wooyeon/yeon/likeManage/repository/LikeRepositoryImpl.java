package com.wooyeon.yeon.likeManage.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wooyeon.yeon.likeManage.domain.QUserLike;
import com.wooyeon.yeon.likeManage.dto.MyUniqueInfoDto;
import com.wooyeon.yeon.likeManage.dto.ResponseProfileDto;
import com.wooyeon.yeon.profileChoice.domain.QUserMatch;
import com.wooyeon.yeon.user.domain.QProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.List;

public class LikeRepositoryImpl implements LikeRepositoryFindProfileList {

    private final JPAQueryFactory queryFactory;

    public LikeRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<ResponseProfileDto> findProfilesWhoLikedMe(MyUniqueInfoDto condition, Pageable pageable) {

        QUserLike userLike = QUserLike.userLike;
        QProfile profile = QProfile.profile;
        // QueryDSL을 사용하여 "나를 좋아요" 한 프로필 리스트 조회
        QueryResults<ResponseProfileDto> results = queryFactory
                .select(Projections.bean(ResponseProfileDto.class,
                        profile.gender,
                        profile.nickname,
                        profile.birthday,
                        profile.gpsLocationInfo,
                        profile.mbti,
                        profile.intro,
                        profile.user.userCode
                ))
                .from(userLike)
                .where(userLike.likeTo.userId.eq(condition.getMyUserid()))
                .fetchResults();

        List<ResponseProfileDto> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content, pageable, total);

        //return query.fetch();
        //return null;
    }

    @Override
    public Page<ResponseProfileDto> findProfilesILiked(MyUniqueInfoDto condition, Pageable pageable) {
        QUserLike userLike = QUserLike.userLike;
        QProfile profile = QProfile.profile;
        // QueryDSL을 사용하여 "내가 좋아요" 한 프로필 리스트 조회
        QueryResults<ResponseProfileDto> results = queryFactory
                .select(Projections.bean(ResponseProfileDto.class,
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

        List<ResponseProfileDto> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<ResponseProfileDto> findProfilesMachedMe(MyUniqueInfoDto condition, Pageable pageable) {
        QUserLike userLike = QUserLike.userLike;
        QUserMatch userMatch = QUserMatch.userMatch;
        QProfile profile = QProfile.profile;
        // QueryDSL을 사용하여 매치된 프로필 리스트 조회

        QueryResults<ResponseProfileDto> results = queryFactory
                .select(Projections.bean(ResponseProfileDto.class,
                        profile.gender,
                        profile.nickname,
                        profile.birthday,
                        profile.gpsLocationInfo,
                        profile.mbti,
                        profile.intro,
                        profile.user.userCode
                ))
                .from(userMatch)
                .where(
                        userMatch.userLike1.likeFrom.userId.eq(condition.getMyUserid()).and(
                                userMatch.userLike1.likeFrom.userId.eq(userMatch.userLike2.likeTo.userId)
                        ).or(
                                userMatch.userLike1.likeFrom.userId.eq(condition.getMyUserid()).and(
                                        userMatch.userLike1.likeTo.userId.eq(userMatch.userLike2.likeFrom.userId)
                                )
                        ).or(
                                userMatch.userLike2.likeFrom.userId.eq(condition.getMyUserid()).and(
                                        userMatch.userLike2.likeFrom.userId.eq(userMatch.userLike1.likeTo.userId)
                                )
                        ).or(
                                userMatch.userLike2.likeFrom.userId.eq(condition.getMyUserid()).and(
                                        userMatch.userLike2.likeTo.userId.eq(userMatch.userLike1.likeFrom.userId)
                                )
                        )
                ).fetchResults();

        List<ResponseProfileDto> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content, pageable, total);
    }
}
