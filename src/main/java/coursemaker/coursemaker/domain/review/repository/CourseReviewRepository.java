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
}
