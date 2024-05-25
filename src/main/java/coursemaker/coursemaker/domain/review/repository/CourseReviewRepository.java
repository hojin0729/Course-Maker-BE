package coursemaker.coursemaker.domain.review.repository;

import coursemaker.coursemaker.domain.review.entity.CourseReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseReviewRepository extends JpaRepository<CourseReview, Long> {
}
