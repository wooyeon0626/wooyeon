package com.wooyeon.yeon.chat.repository;

import com.wooyeon.yeon.chat.domain.Chat;
import com.wooyeon.yeon.profileChoice.domain.UserMatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    Optional<Chat> findFirstByUserMatchOrderBySendTimeDesc(UserMatch userMatch);
    List<Chat> findAllByMessageContains(String searchWord);
    Long findCountByIsChecked(boolean isChecked);
    List<Chat> findAllByUserMatchIdOOrderBySendTime(Long matchId);
}