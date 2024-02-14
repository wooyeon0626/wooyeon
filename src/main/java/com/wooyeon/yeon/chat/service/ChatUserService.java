package com.wooyeon.yeon.chat.service;

import com.wooyeon.yeon.chat.domain.Block;
import com.wooyeon.yeon.chat.domain.Report;
import com.wooyeon.yeon.chat.dto.ChatUserDto;
import com.wooyeon.yeon.chat.repository.BlockRepository;
import com.wooyeon.yeon.chat.repository.ReportRepository;
import com.wooyeon.yeon.exception.ExceptionCode;
import com.wooyeon.yeon.exception.WooyeonException;
import com.wooyeon.yeon.likeManage.domain.UserLike;
import com.wooyeon.yeon.likeManage.repository.LikeRepository;
import com.wooyeon.yeon.profileChoice.domain.UserMatch;
import com.wooyeon.yeon.profileChoice.repository.MatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatUserService {
    private final BlockRepository blockRepository;
    private final MatchRepository matchRepository;
    private final LikeRepository likeRepository;
    private final ReportRepository reportRepository;

    @Transactional
    public void blockUser(ChatUserDto.ChatUserRequest chatUserRequest) {
        Long userId = chatUserRequest.getUserId();
        Long matchUserId = chatUserRequest.getMatchUserId();

        Block block = Block.builder()
                .fromId(userId)
                .toId(matchUserId)
                .BlockTime(LocalDateTime.now())
                .build();

        blockRepository.save(block);

        //차단된 유저 매치 목록에서 삭제
        List<UserMatch> userMatchList = matchRepository.findAllByUser1(userId)
                .orElseThrow(() -> new WooyeonException(ExceptionCode.USER_NOT_FOUND));

        matchRepository.deleteAll(userMatchList);

        //차단된 유저 좋아요 목록에서 삭제
        List<UserLike> userLikeList = likeRepository.findAllByLikeFrom(userId)
                .orElseThrow(() -> new WooyeonException(ExceptionCode.USER_NOT_FOUND));

        likeRepository.deleteAll(userLikeList);

    }

    @Transactional
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
            optionalReport.setCount(optionalReport.getCount() + 1);
            optionalReport.setUpdateTime(LocalDateTime.now());
            reportRepository.save(optionalReport);
        }
    }
}
