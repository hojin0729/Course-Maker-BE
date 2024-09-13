package coursemaker.coursemaker.domain.review.repository;

import coursemaker.coursemaker.domain.member.entity.Member;
import coursemaker.coursemaker.domain.review.entity.DestinationReview;
import coursemaker.coursemaker.domain.review.entity.DestinationReviewRecommendation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DestinationReviewRecommendationRepository extends JpaRepository<DestinationReviewRecommendation, Long> {
    Optional<DestinationReviewRecommendation> findByDestinationReviewAndMember(DestinationReview review, Member member);
}
