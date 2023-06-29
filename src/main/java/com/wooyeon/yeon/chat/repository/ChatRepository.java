package com.wooyeon.yeon.chat.repository;

import com.wooyeon.yeon.chat.domain.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Integer> {

}