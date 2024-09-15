package coursemaker.coursemaker.domain.review.service;

import coursemaker.coursemaker.domain.review.dto.RequestCourseDto;
import coursemaker.coursemaker.domain.review.entity.CourseReview;
import coursemaker.coursemaker.util.CourseMakerPagination;
import org.springframework.data.domain.Pageable;


public interface CourseReviewService {
    // 코스 리뷰 등록
    CourseReview save(RequestCourseDto requestCourseDto, Long courseId);
    // 코스 리뷰 수정
    CourseReview update(Long courseId, RequestCourseDto requestCourseDto, String nickname);
    // 코스 리뷰 삭제
    void delete(Long id);
    // id 기반으로 특정 코스리뷰 조회하는 메서드
    CourseReview findById(Long id);
    // 코스 리뷰 전체 보기
    CourseMakerPagination<CourseReview> findAllByCourseId(Long courseId, Pageable pageable, OrderBy orderBy);
    // 코스 평균 평점 계산
    Double getAverageRating(Long courseId);

    Integer getReviewCount(Long courseId);

    CourseMakerPagination<CourseReview> findByMemberNickname(String nickname, Pageable pageable);

    void addRecommend(Long reviewId, String nickname);
    void removeRecommend(Long reviewId, String nickname);

    boolean isReviewRecommendedByUser(Long reviewId, String nickname);
}

