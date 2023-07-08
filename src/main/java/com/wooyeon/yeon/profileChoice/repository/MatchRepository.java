package com.wooyeon.yeon.profileChoice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.regex.Matcher;

public interface MatchRepository extends JpaRepository<Matcher, Long> {
}
