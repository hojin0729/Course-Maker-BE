package coursemaker.coursemaker.domain.wish.service;

import coursemaker.coursemaker.domain.wish.entity.CourseWish;
import coursemaker.coursemaker.domain.wish.entity.DestinationWish;

import java.util.List;

public interface DestinationWishService {
    List<DestinationWish> getAllDestinationWishes();

    List<DestinationWish> getDestinationWishesByNickname(String nickname);

    /* 코스 찜목록 닉네임으로 조회 */

    DestinationWish addDestinationWish(Long destinationId, Long memberId);

    void cancelDestinationWish(Long destinationId, Long memberId);
}
