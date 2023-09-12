package com.wooyeon.yeon.likeManage.repository;

import com.wooyeon.yeon.likeManage.dto.ProfileDto;
import com.wooyeon.yeon.likeManage.dto.ProfileThatLikesMeCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LikeRepositoryFindProfileList {
    List<ProfileDto> findProfilesWhoLikedMe(ProfileThatLikesMeCondition condition);
}
