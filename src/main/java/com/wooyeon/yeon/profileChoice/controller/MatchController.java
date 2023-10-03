package com.wooyeon.yeon.profileChoice.controller;

import com.wooyeon.yeon.profileChoice.dto.RecommandUserCondition;
import com.wooyeon.yeon.profileChoice.dto.RecommandUserDto;
import com.wooyeon.yeon.profileChoice.repository.MatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MatchController {
    private final MatchRepository matchRepository;

    /**
     * Profile 리스트를 조회합니다.
     *
     * @param condition
     * @param pageable
     * @return Page(RecommandUserDto)
     */
    @GetMapping("/recommand/profilelist")
    public Page<RecommandUserDto> responseProfileList(RecommandUserCondition condition, Pageable pageable) {
        //지금은 랜덤이지만 내가 좋아요 안한 사람들만 보내줘야함/ 내가 좋아요 했으면 프로필 리스트에 뜨면 안됨.
        return matchRepository.searchUserProfileSimple(condition, pageable);
    }
}
