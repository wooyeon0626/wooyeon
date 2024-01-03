package com.wooyeon.yeon.likeManage.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wooyeon.yeon.likeManage.domain.QUserLike;
import com.wooyeon.yeon.likeManage.dto.MyUniqueInfoDto;
import com.wooyeon.yeon.likeManage.dto.ResponseProfileDto;
import com.wooyeon.yeon.profileChoice.domain.QUserMatch;
import com.wooyeon.yeon.profileChoice.domain.UserMatch;
import com.wooyeon.yeon.user.domain.QProfile;
import com.wooyeon.yeon.user.domain.QUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.ArrayList;
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

        // QueryDSL을 사용하여 매치된 프로필 리스트 조회

        // 나와 매치된 사용자의 ID를 찾습니다.
        // case1 : user_match의 user2컬럼이 본인일 때 user1의 id가 나외 매치된 id
        List<Long> user1Ids = queryFactory
                .select(userMatch.user1.profile.id)
                .from(userMatch)
                .where(userMatch.user2.userId.eq(condition.getMyUserid()))
                .fetch();

        // case2 : user_match의 user1컬럼이 본인일 때 user2의 id가 나외 매치된 id
        List<Long> user2Ids = queryFactory
                .select(userMatch.user2.profile.id)
                .from(userMatch)
                .where(userMatch.user1.userId.eq(condition.getMyUserid()))
                .fetch();

        List<Long> likedUserIds = new ArrayList<>();
        likedUserIds.addAll(user1Ids);
        likedUserIds.addAll(user2Ids);

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
}
