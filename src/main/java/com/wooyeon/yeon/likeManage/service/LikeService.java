package com.wooyeon.yeon.likeManage.service;

import com.wooyeon.yeon.likeManage.repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
}
