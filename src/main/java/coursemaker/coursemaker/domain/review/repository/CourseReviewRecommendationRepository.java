package coursemaker.coursemaker.domain.review.repository;

import coursemaker.coursemaker.domain.member.entity.Member;
import coursemaker.coursemaker.domain.review.entity.CourseReview;
import coursemaker.coursemaker.domain.review.entity.CourseReviewRecommendation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CourseReviewRecommendationRepository extends JpaRepository<CourseReviewRecommendation, Long> {
    Optional<CourseReviewRecommendation> findByCourseReviewAndMember(CourseReview review, Member member);
}