package com.wooyeon.yeon.likeManage.service;

import com.wooyeon.yeon.likeManage.domain.UserLike;
import com.wooyeon.yeon.likeManage.dto.CreateLikeResponse;
import com.wooyeon.yeon.likeManage.repository.LikeRepository;
import com.wooyeon.yeon.profileChoice.repository.MatchRepository;
import com.wooyeon.yeon.profileChoice.service.MatchService;
import com.wooyeon.yeon.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author heesoo
 */
@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final MatchService matchService;

    /**
     * like를 생성하는 메서드
     * 서로 like 이면 매치도 생성한다.
     *
     * @param likeFromUser
     * @param likeToUser
     * @return Controller에 넘길 response : responseMessage 포함된 객체
     */
    @Transactional
    public CreateLikeResponse saveUserLike(User likeFromUser, User likeToUser) {
        UserLike userLike = UserLike.builder()
                .likeTo(likeToUser)
                .likeFrom(likeFromUser)
                .build();

        likeRepository.save(userLike);

        // 이 부분은 repository로 빼아할 것 같다.
        CreateLikeResponse response;
        boolean isMatch1 = checkMatch(likeFromUser.getUserId(), likeToUser.getUserId());
        boolean isMatch2 = checkMatch(likeToUser.getUserId(), likeFromUser.getUserId());

        if (isMatch1 && isMatch2) {
            response = new CreateLikeResponse("매치됨", "");
            //UserLike userLike1 = likeRepository.findByLikeFromAndLikeTo(likeFromUser, likeToUser);
            // UserLike userLike2 = likeRepository.findByLikeFromAndLikeTo(likeToUser, likeFromUser);
            //matchService.createMatch(userLike1, userLike2);
            matchService.createMatch(likeFromUser, likeToUser);
        } else {
            response = new CreateLikeResponse("매치안됨.", "");
        }
        return response;
    }

    /**
     * 매치 되었는지 확인 하는 메서드
     *
     * @param userId1
     * @param userId2
     * @return
     */
    public boolean checkMatch(Long userId1, Long userId2) {
        long matchCount = likeRepository.countMatch(userId1, userId2);
        return matchCount > 0;
    }

    /**
     * 매치 되었을 때 기존 user_like 테이블의 좋아요를 삭제함.
     *
     * @param userId1
     * @param userId2
     */
    @Transactional
    public void deleteUserLikeRow(Long userId1, Long userId2) {
        // 1. 일단 삭제할 user_like 의 id를 구해야함
        // 1-1 구하기 위해 from 이 userId1이면서 to 가 userId2인 user 선택
        // 1-2 구하기 위해 to 가 userId1이면서 from 이 userId2인 user 선택
        // 2.해당 user_like id 삭제 연산 실행
        User user1 = likeRepository.findUserByUserId(userId1);
        User user2 = likeRepository.findUserByUserId(userId2);

        UserLike targetDeleteUserLike1 = likeRepository.findByLikeFromAndLikeTo(user1, user2);
        UserLike targetDeleteUserLike2 = likeRepository.findByLikeFromAndLikeTo(user2, user1);

        likeRepository.deleteById(targetDeleteUserLike1.getLikeId());
        likeRepository.deleteById(targetDeleteUserLike2.getLikeId());
    }

}