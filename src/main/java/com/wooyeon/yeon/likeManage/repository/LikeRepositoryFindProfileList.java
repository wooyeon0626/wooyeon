package com.wooyeon.yeon.likeManage.repository;

import com.wooyeon.yeon.likeManage.dto.MyUniqueInfoDto;
import com.wooyeon.yeon.likeManage.dto.ResponseProfileDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LikeRepositoryFindProfileList {
    //각 메소드들의 상세 구현은 LikeRepositoryImpl에 구현 됨.
    Page<ResponseProfileDto> findProfilesWhoLikedMe(MyUniqueInfoDto condition, Pageable pageable);

    Page<ResponseProfileDto> findProfilesILiked(MyUniqueInfoDto condition, Pageable pageable);

    Page<ResponseProfileDto> findProfilesMachedMe(MyUniqueInfoDto condition, Pageable pageable);
}
