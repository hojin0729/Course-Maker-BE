package coursemaker.coursemaker.domain.like.service;

import coursemaker.coursemaker.domain.like.dto.CourseLikeRequestDto;
import coursemaker.coursemaker.domain.like.dto.CourseLikeResponseDto;

import java.util.List;

public interface CourseLikeService {

    List<CourseLikeResponseDto> getAllCourseLikes();

    List<CourseLikeResponseDto> getCourseLikesByNickname(String nickname);

    CourseLikeResponseDto addCourseLike(CourseLikeRequestDto requestDto);

    void cancelCourseLike(Long courseId, String nickname);

    /* 특정 코스에 대한 찜 목록 조회 */
    List<CourseLikeResponseDto> getLikesByCourseId(Long courseId);

    Integer getCourseLikeCount(Long courseId);
}
