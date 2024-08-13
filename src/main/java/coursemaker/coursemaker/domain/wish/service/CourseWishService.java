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
}
