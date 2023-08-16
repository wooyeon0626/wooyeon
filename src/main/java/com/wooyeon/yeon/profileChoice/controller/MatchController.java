package com.wooyeon.yeon.profileChoice.controller;

import com.wooyeon.yeon.profileChoice.dto.RecommandUserCondition;
import com.wooyeon.yeon.profileChoice.dto.RecommandUserDto;
import com.wooyeon.yeon.profileChoice.repository.MatchRepository;
import com.wooyeon.yeon.profileChoice.service.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MatchController {
    private final MatchService matchService;
    private final MatchRepository matchRepository;

    @GetMapping("/recommand/profilelist")
    public Page<RecommandUserDto> responseProfileList(RecommandUserCondition condition, Pageable pageable) {
        return matchRepository.searchUserProfileSimple(condition, pageable);
    }
}
