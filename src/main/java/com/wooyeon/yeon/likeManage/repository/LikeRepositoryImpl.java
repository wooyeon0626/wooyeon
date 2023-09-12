package com.wooyeon.yeon.likeManage.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wooyeon.yeon.likeManage.domain.QUserLike;
import com.wooyeon.yeon.likeManage.domain.UserLike;
import com.wooyeon.yeon.likeManage.dto.ProfileDto;
import com.wooyeon.yeon.likeManage.dto.ProfileThatLikesMeCondition;
import com.wooyeon.yeon.profileChoice.dto.RecommandUserDto;
import com.wooyeon.yeon.user.domain.QProfile;
import com.wooyeon.yeon.user.domain.QUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.List;

import static com.wooyeon.yeon.user.domain.QProfile.profile;
import static com.wooyeon.yeon.user.domain.QUser.user;

public class LikeRepositoryImpl implements LikeRepositoryFindProfileList {

    private final JPAQueryFactory queryFactory;

    public LikeRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<ProfileDto> findProfilesWhoLikedMe(ProfileThatLikesMeCondition condition) {

        QUserLike userLike = QUserLike.userLike;
        QProfile profile = QProfile.profile;

        // QueryDSL을 사용하여 "나를 좋아요" 한 프로필 리스트 조회
//        JPAQuery<UserLike> query = queryFactory
//                .select(profile)
//                .from(profile)
//                .join(userLike.likeFrom, userLike)
//                .join(profile.user, user) // User 엔티티와 조인합니다.
//                .where(user.userCode.eq(condition.getLikedMeUserCode()));

//        return query.fetch();

        return null;
    }
}
