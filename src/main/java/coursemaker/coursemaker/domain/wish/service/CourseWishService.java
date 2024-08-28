package coursemaker.coursemaker.domain.wish.service;

import coursemaker.coursemaker.domain.wish.dto.CourseWishRequestDto;
import coursemaker.coursemaker.domain.wish.dto.CourseWishResponseDto;
import coursemaker.coursemaker.domain.wish.entity.CourseWish;

import java.util.List;

public interface CourseWishService {

    List<CourseWishResponseDto> getAllCourseWishes();

    List<CourseWishResponseDto> getCourseWishesByNickname(String nickname);

    CourseWishResponseDto addCourseWish(CourseWishRequestDto requestDto);

    void cancelCourseWish(Long courseId, String nickname);

    /* 특정 코스에 대한 찜 목록 조회 */
    List<CourseWishResponseDto> getWishesByCourseId(Long courseId);

    Integer getCourseWishCount(Long courseId);

    Boolean isCourseWishedByUser(Long courseId, String nickname);
}
