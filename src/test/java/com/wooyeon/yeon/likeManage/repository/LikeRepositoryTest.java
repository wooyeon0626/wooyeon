package com.wooyeon.yeon.likeManage.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false)//테스트 끝나고 다 날리는거 방지
class LikeRepositoryTest {
//    @Autowired LikeRepository likeRepository;
//
//    @Test
//    public void testLike() {
//
//    }
}