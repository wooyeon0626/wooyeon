package com.wooyeon.yeon.likeManage.repository;

import com.wooyeon.yeon.likeManage.dto.ProfileDto;
import com.wooyeon.yeon.likeManage.dto.ProfileThatLikesMeCondition;
import com.wooyeon.yeon.likeManage.dto.ResponseLikeMe;
import com.wooyeon.yeon.user.domain.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LikeRepositoryFindProfileList {
    Page<ResponseLikeMe> findProfilesWhoLikedMe(ProfileThatLikesMeCondition condition, Pageable pageable);//ProfileThatLikesMeCondition condition
}
