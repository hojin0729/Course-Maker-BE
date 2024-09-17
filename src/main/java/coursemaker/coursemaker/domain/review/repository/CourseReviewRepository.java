package coursemaker.coursemaker.domain.review.repository;

import coursemaker.coursemaker.domain.course.entity.TravelCourse;
import coursemaker.coursemaker.domain.member.entity.Member;
import coursemaker.coursemaker.domain.review.entity.CourseReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseReviewRepository extends JpaRepository<CourseReview, Long> {
    Optional<CourseReview> findByMemberAndTravelCourse(Member member, TravelCourse travelCourse);
    Page<CourseReview> findByTravelCourseId(Long courseId, Pageable pageable);
    List<CourseReview> findByTravelCourseId(Long courseId);

    Integer countByTravelCourseId(Long courseId);

    Page<CourseReview> findByMemberNicknameAndDeletedAtIsNull(String nickname, Pageable pageable);

    Page<CourseReview> findAllByTravelCourseIdAndMemberNickname(Long courseId, String nickname, Pageable pageable);
    Page<CourseReview> findAllByTravelCourseIdAndMemberNicknameNot(Long courseId, String nickname, Pageable pageable);

    // 별점 높은 순
    Page<CourseReview> findAllByTravelCourseIdAndMemberNicknameOrderByRatingDesc(Long courseId, String nickname, Pageable pageable);
    Page<CourseReview> findAllByTravelCourseIdAndMemberNicknameNotOrderByRatingDesc(Long courseId, String nickname, Pageable pageable);

    // 별점 낮은 순
    Page<CourseReview> findAllByTravelCourseIdAndMemberNicknameOrderByRatingAsc(Long courseId, String nickname, Pageable pageable);
    Page<CourseReview> findAllByTravelCourseIdAndMemberNicknameNotOrderByRatingAsc(Long courseId, String nickname, Pageable pageable);

    // 최신순
    Page<CourseReview> findAllByTravelCourseIdAndMemberNicknameOrderByCreatedAtDesc(Long courseId, String nickname, Pageable pageable);
    Page<CourseReview> findAllByTravelCourseIdAndMemberNicknameNotOrderByCreatedAtDesc(Long courseId, String nickname, Pageable pageable);

    // 추천순
    Page<CourseReview> findAllByTravelCourseIdAndMemberNicknameOrderByRecommendCountDesc(Long courseId, String nickname, Pageable pageable);
    Page<CourseReview> findAllByTravelCourseIdAndMemberNicknameNotOrderByRecommendCountDesc(Long courseId, String nickname, Pageable pageable);
}
