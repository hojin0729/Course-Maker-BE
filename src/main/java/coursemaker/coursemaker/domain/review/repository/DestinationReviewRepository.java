package coursemaker.coursemaker.domain.review.repository;

import coursemaker.coursemaker.domain.destination.entity.Destination;
import coursemaker.coursemaker.domain.member.entity.Member;
import coursemaker.coursemaker.domain.review.entity.DestinationReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DestinationReviewRepository extends JpaRepository<DestinationReview, Long>{
    Optional<DestinationReview> findByMemberAndDestination(Member member, Destination destination);
    Page<DestinationReview> findByDestination(Destination destination, Pageable pageable);
    List<DestinationReview> findByDestinationId(Long destinationId);

    Integer countByDestinationId(Long destinationId);
}
