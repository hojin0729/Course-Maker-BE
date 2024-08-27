package coursemaker.coursemaker.domain.wish.service;

import coursemaker.coursemaker.domain.wish.dto.DestinationWishRequestDto;
import coursemaker.coursemaker.domain.wish.dto.DestinationWishResponseDto;
import coursemaker.coursemaker.domain.wish.entity.DestinationWish;

import java.util.List;

public interface DestinationWishService {
    List<DestinationWishResponseDto> getAllDestinationWishes();

    List<DestinationWishResponseDto> getDestinationWishesByNickname(String nickname);

    /* 목적지 찜목록 닉네임으로 조회 */

    DestinationWishResponseDto addDestinationWish(DestinationWishRequestDto requestDto);

    void cancelDestinationWish(Long destinationId, String nickname);

    /* 특정 목적지에 대한 찜 목록 조회 */
    List<DestinationWishResponseDto> getWishesByDestinationId(Long destinationId);

    /* 목적지별 찜된 수 조회 */
    Integer getDestinationWishCount(Long destinationId);
}
