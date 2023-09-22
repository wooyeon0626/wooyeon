package com.wooyeon.yeon.likeManage.service;

import com.wooyeon.yeon.likeManage.domain.UserLike;
import com.wooyeon.yeon.likeManage.dto.CreateLikeResponse;
import com.wooyeon.yeon.likeManage.dto.LikeDto;
import com.wooyeon.yeon.likeManage.repository.LikeRepository;
import com.wooyeon.yeon.profileChoice.repository.MatchRepository;
import com.wooyeon.yeon.profileChoice.service.MatchService;
import com.wooyeon.yeon.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final MatchRepository matchRepository;
    private final MatchService matchService;

    //like 생성
    @Transactional
    public CreateLikeResponse saveUserLike(User likeFromUser, User likeToUser) {
        UserLike userLike = UserLike.builder()
                .likeTo(likeToUser)
                .likeFrom(likeFromUser)
                .build();
        likeRepository.save(userLike);
        CreateLikeResponse response;
        boolean isMatch1 = checkMatch(likeFromUser.getUserId(), likeToUser.getUserId());
        boolean isMatch2 = checkMatch(likeToUser.getUserId(), likeFromUser.getUserId());

        if (isMatch1 && isMatch2) {
            response = new CreateLikeResponse("매치됨");
            // 이부분은 repository로 빼아할 것 같다. 코드분리도
            // 새로 new를 해서 save하면 userLike테이블에 같은 row가 또 생성되니
            // new하지말고 기존 UserLike를 가져오자.
            UserLike userLike1 = likeRepository.findByLikeFromAndLikeTo(likeFromUser, likeToUser);
            UserLike userLike2 = likeRepository.findByLikeFromAndLikeTo(likeToUser, likeFromUser);

            //여기서 영속성 컨텍스트에 저장을 해줘야 에러가 안남
//            likeRepository.save(userLike1);
//            likeRepository.save(userLike2);

            matchService.createMatch(userLike1, userLike2);
        } else {
            response = new CreateLikeResponse("매치안됨.");
        }
        return response;
    }

    //매치 되었는지 확인 하는 메서드
    public boolean checkMatch(Long userId1, Long userId2) {
        long matchCount = likeRepository.countMatch(userId1, userId2);
        return matchCount > 0;
    }

    //매치되었는지 확인 하는 메서드
//    public boolean checkMatch(User user1, User user2) {
//        UserLike like1 = likeRepository.findByLikeFromAndLikeTo(user1, user2);
//        UserLike like2 = likeRepository.findByLikeFromAndLikeTo(user2, user1);
//
//        return like1 != null && like2 != null;
//    }

//     내가 한 좋아요 조회
//    public List<LikeDto> findMyLikeList(Long likeId) {
//        userlike의 likefrom == 본인 이름인것을 반환.
//        List<UserLike> userLikeList = likeRepository.findBy
//    }

//    private LikeDto convertToLikeDto(UserLike userLike) {
//        LikeDto dto = new LikeDto();
//        dto.setLikeId(userLike.getLikeId());
//        dto.setLikeToUserId(userLike.getLikeTo().getUserId());
//        dto.setLikeFromUserId(userLike.getLikeFrom().getUserId());
//
//        return dto;
//    }

    // 내가 받은 좋아요 조회

}