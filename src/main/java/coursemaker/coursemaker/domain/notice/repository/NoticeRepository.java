package coursemaker.coursemaker.domain.notice.repository;

import coursemaker.coursemaker.domain.review.entity.CourseReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeRepository extends JpaRepository<CourseReview, Long> {
}
