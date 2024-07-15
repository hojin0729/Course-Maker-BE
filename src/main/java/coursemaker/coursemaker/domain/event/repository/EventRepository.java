package coursemaker.coursemaker.domain.event.repository;

import coursemaker.coursemaker.domain.review.entity.CourseReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<CourseReview, Long> {
}
