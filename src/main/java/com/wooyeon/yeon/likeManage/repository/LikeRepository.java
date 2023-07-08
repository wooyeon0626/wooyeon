package com.wooyeon.yeon.likeManage.repository;

import com.wooyeon.yeon.likeManage.domain.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
}
