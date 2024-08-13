package coursemaker.coursemaker.domain.wish.service;

import coursemaker.coursemaker.domain.wish.dto.DestinationWishRequestDto;
import coursemaker.coursemaker.domain.wish.dto.DestinationWishResponseDto;
import coursemaker.coursemaker.domain.wish.entity.DestinationWish;

import java.util.List;

public interface DestinationWishService {
    List<DestinationWishResponseDto> getAllDestinationWishes();

    List<DestinationWishResponseDto> getDestinationWishesByNickname(String nickname);

    /* 코스 찜목록 닉네임으로 조회 */

    DestinationWishResponseDto addDestinationWish(DestinationWishRequestDto requestDto);

    void cancelDestinationWish(Long destinationId, String nickname);
}
