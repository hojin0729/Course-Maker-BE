package coursemaker.coursemaker.domain.like.service;

import coursemaker.coursemaker.domain.like.dto.DestinationLikeRequestDto;
import coursemaker.coursemaker.domain.like.dto.DestinationLikeResponseDto;

import java.util.List;

public interface DestinationLikeService {
    List<DestinationLikeResponseDto> getAllDestinationLikes();

    List<DestinationLikeResponseDto> getDestinationLikesByNickname(String nickname);

    /* 목적지 찜목록 닉네임으로 조회 */

    DestinationLikeResponseDto addDestinationLike(DestinationLikeRequestDto requestDto);

    void cancelDestinationLike(Long destinationId, String nickname);

    /* 특정 목적지에 대한 찜 목록 조회 */
    List<DestinationLikeResponseDto> getLikesByDestinationId(Long destinationId);

    /* 목적지별 찜된 수 조회 */
    Integer getDestinationLikeCount(Long destinationId);
}
