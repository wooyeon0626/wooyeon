package com.wooyeon.yeon.likeManage.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wooyeon.yeon.likeManage.domain.QUserLike;
import com.wooyeon.yeon.likeManage.dto.MyUniqueInfoDto;
import com.wooyeon.yeon.likeManage.dto.ResponseProfileDto;
import com.wooyeon.yeon.profileChoice.domain.QUserMatch;
import com.wooyeon.yeon.user.domain.QProfile;
import com.wooyeon.yeon.user.domain.QUser;
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
    public Page<ResponseProfileDto> findProfilesWhoLikedMe(MyUniqueInfoDto condition, Pageable pageable) {
        QUser user = QUser.user;
        QUserLike userLike = QUserLike.userLike;
        //QProfile profile = QProfile.profile;

        // "나를 좋아요"한 사용자의 ID를 찾습니다.
        List<Long> likedUserIds = queryFactory
                .select(user.profile.id) // userLike.likeFrom.userId 대신 프로필 ID를 선택합니다.
                .from(userLike)
                .join(userLike.likeFrom, user) // "likeFrom"에 해당하는 사용자 정보를 가져오기 위한 조인.
                .where(userLike.likeTo.userId.eq(condition.getMyUserid()))
                .fetch();

        // QueryDSL을 사용하여 "나를 좋아요" 한 프로필 리스트 조회
        QueryResults<ResponseProfileDto> results = queryFactory
                .select(Projections.bean(ResponseProfileDto.class,
                        profile.gender,
                        profile.nickname,
                        profile.birthday,
                        profile.gpsLocationInfo,
                        profile.mbti,
                        profile.intro
                ))
                .from(profile)
                .where(profile.user.userId.in(likedUserIds))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<ResponseProfileDto> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<ResponseProfileDto> findProfilesILiked(MyUniqueInfoDto condition, Pageable pageable) {
        QProfile profile = QProfile.profile;
        QUser user = QUser.user;
        QUserLike userLike = QUserLike.userLike;

        // "내가 좋아요"한 사용자의 ID를 찾습니다.
        List<Long> likedUserIds = queryFactory
                .select(user.profile.id) // 프로필 ID를 선택합니다.
                .from(userLike)
                .join(userLike.likeTo, user) // "likeFrom"에 해당하는 사용자 정보를 가져오기 위한 조인.
                .where(userLike.likeFrom.userId.eq(condition.getMyUserid()))
                .fetch();
        // QueryDSL을 사용하여 "내가 좋아요" 한 프로필 리스트 조회
        QueryResults<ResponseProfileDto> results = queryFactory
                .select(Projections.bean(ResponseProfileDto.class,
                        profile.gender,
                        profile.nickname,
                        profile.birthday,
                        profile.gpsLocationInfo,
                        profile.mbti,
                        profile.intro
                ))
                .from(profile)
                .where(profile.user.userId.in(likedUserIds))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<ResponseProfileDto> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<ResponseProfileDto> findProfilesMachedMe(MyUniqueInfoDto condition, Pageable pageable) {
        QUserMatch userMatch = QUserMatch.userMatch;
        QProfile profile = QProfile.profile;
        QUser user = QUser.user;

        // QueryDSL을 사용하여 매치된 프로필 리스트 조회
       /* QueryResults<ResponseProfileDto> results = queryFactory
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
                .join(profile.user, user)
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
                ).fetchResults();*/

        // 나와 매치된 사용자의 ID를 찾습니다.
        /*List<Long> likedUserIds = queryFactory
                .select(user.profile.id) // userLike.likeFrom.userId 대신 프로필 ID를 선택합니다.
                .from(userMatch)
                .join(userMatch.userLike1.likeFrom, user) // "likeTo"에 해당하는 사용자 정보를 가져오기 위한 조인.
                .where(userLike.likeTo.userId.eq(condition.getMyUserid()))
                .fetch();
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
                .from(profile)
                .join(profile.user, user)
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
        return new PageImpl<>(content, pageable, total);*/
        return null;
    }
}
