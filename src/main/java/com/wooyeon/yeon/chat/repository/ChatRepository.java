package com.wooyeon.yeon.chat.repository;

import com.wooyeon.yeon.chat.domain.Chat;
import com.wooyeon.yeon.profileChoice.domain.UserMatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Integer> {
    Chat findFirstByUserMatchOrderBySendTimeDesc(UserMatch userMatch);
    List<Chat> findAllByMessageContains(String searchWord);
}