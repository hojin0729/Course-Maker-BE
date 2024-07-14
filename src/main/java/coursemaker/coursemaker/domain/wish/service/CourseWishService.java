package coursemaker.coursemaker.domain.wish.service;

import coursemaker.coursemaker.domain.wish.entity.CourseWish;

import java.util.List;

public interface CourseWishService {

    List<CourseWish> getAllCourseWishes();

    CourseWish getCourseWishById(Long id);

    CourseWish addCourseWish(Long courseId);
    /* 찜하기 취소 */
    void cancelCourseWish(Long courseId);
}
