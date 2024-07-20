package coursemaker.coursemaker.domain.wish.service;

import coursemaker.coursemaker.domain.wish.entity.CourseWish;

import java.util.List;

public interface CourseWishService {

    List<CourseWish> getAllCourseWishes();

    List<CourseWish> getCourseWishesByNickname(String nickname);

    CourseWish addCourseWish(Long courseId, Long memberId);

    void cancelCourseWish(Long courseId, Long memberId);
}
