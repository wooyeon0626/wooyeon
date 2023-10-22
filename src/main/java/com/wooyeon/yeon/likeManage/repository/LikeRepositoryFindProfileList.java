package com.wooyeon.yeon.likeManage.repository;

import com.wooyeon.yeon.likeManage.dto.MyUniqueInfoDto;
import com.wooyeon.yeon.likeManage.dto.ResponseProfileDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LikeRepositoryFindProfileList {
    Page<ResponseProfileDto> findProfilesWhoLikedMe(MyUniqueInfoDto condition, Pageable pageable);

    Page<ResponseProfileDto> findProfilesILiked(MyUniqueInfoDto condition, Pageable pageable);

    Page<ResponseProfileDto> findProfilesMachedMe(MyUniqueInfoDto condition, Pageable pageable);
}
