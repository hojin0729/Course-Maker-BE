package coursemaker.coursemaker.domain.review.repository;

import coursemaker.coursemaker.domain.review.entity.DestinationReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DestinationReviewRepository extends JpaRepository<DestinationReview, Long>{
}
