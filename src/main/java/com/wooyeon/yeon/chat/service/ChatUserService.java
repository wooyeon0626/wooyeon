package com.wooyeon.yeon.chat.service;

import com.google.api.gax.rpc.NotFoundException;
import com.wooyeon.yeon.chat.domain.Block;
import com.wooyeon.yeon.chat.domain.Report;
import com.wooyeon.yeon.chat.dto.ChatUserDto;
import com.wooyeon.yeon.chat.repository.BlockRepository;
import com.wooyeon.yeon.chat.repository.ReportRepository;
import com.wooyeon.yeon.likeManage.domain.UserLike;
import com.wooyeon.yeon.likeManage.repository.LikeRepository;
import com.wooyeon.yeon.profileChoice.domain.UserMatch;
import com.wooyeon.yeon.profileChoice.repository.MatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatUserService {

    private final BlockRepository blockRepository;
    private final MatchRepository matchRepository;
    private final LikeRepository likeRepository;
    private final ReportRepository reportRepository;

    @Transactional(readOnly = true)
    public void blockUser(ChatUserDto.ChatUserRequest chatUserRequest) {
        // 임시 유저 아이디
        Long userId = 1l;
        Long matchUserId = chatUserRequest.getMatchUserId();

        Block block = Block.builder()
                .fromId(userId)
                .toId(matchUserId)
                .BlockTime(LocalDateTime.now())
                .build();

        //차단된 유저 매치 목록에서 삭제
        UserMatch userMatch = matchRepository.findAllByUserLike1OOrUserLike2(userId)
                .orElseThrow(() -> new IllegalArgumentException("not found user"));

        matchRepository.delete(userMatch);

        List deleteUserLikeList = new ArrayList<UserLike>();

        //차단된 유저 좋아요 목록에서 삭제
        UserLike userLike1 = likeRepository.findByLikeFromAndLikeTo(userId, matchUserId)
                .orElseThrow(() -> new IllegalArgumentException("not found user or matching user"));
        deleteUserLikeList.add(userLike1);

        UserLike userLike2 = likeRepository.findByLikeFromAndLikeTo(userId, matchUserId)
                .orElseThrow(() -> new IllegalArgumentException("not found user or matching user"));
        deleteUserLikeList.add(userLike2);

        likeRepository.deleteAll(deleteUserLikeList);

        blockRepository.save(block);
    }

    @Transactional(readOnly = true)
    public void reportUser(ChatUserDto.ChatUserRequest chatUserDto) {
        Report optionalReport = reportRepository.findByReportUser(chatUserDto.getMatchUserId());

        if (null == optionalReport) {
            Report report = Report.builder()
                    .reportUser(chatUserDto.getMatchUserId())
                    .count(1)
                    .updateTime(LocalDateTime.now())
                    .build();
            reportRepository.save(report);
        } else {
            int upCount = optionalReport.getCount() + 1;
            optionalReport.setCount(upCount);
            optionalReport.setUpdateTime(LocalDateTime.now());
            reportRepository.save(optionalReport);
        }
    }
}
