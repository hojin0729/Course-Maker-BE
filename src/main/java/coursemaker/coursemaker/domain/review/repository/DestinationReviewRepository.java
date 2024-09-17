package coursemaker.coursemaker.domain.review.repository;

import coursemaker.coursemaker.domain.destination.entity.Destination;
import coursemaker.coursemaker.domain.member.entity.Member;
import coursemaker.coursemaker.domain.review.entity.CourseReview;
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
    Page<DestinationReview> findAllByDestinationId(Long destinationId, Pageable pageable);
    List<DestinationReview> findByDestinationId(Long destinationId);

    Integer countByDestinationId(Long destinationId);

    Page<DestinationReview> findByMemberNicknameAndDeletedAtIsNull(String nickname, Pageable pageable);

    Page<DestinationReview> findAllByDestinationIdAndMemberNickname(Long destinationId, String nickname, Pageable pageable);
    Page<DestinationReview> findAllByDestinationIdAndMemberNicknameNot(Long destinationId, String nickname, Pageable pageable);

    // 별점 높은 순
    Page<DestinationReview> findAllByDestinationIdAndMemberNicknameOrderByRatingDesc(Long destinationId, String nickname, Pageable pageable);
    Page<DestinationReview> findAllByDestinationIdAndMemberNicknameNotOrderByRatingDesc(Long destinationId, String nickname, Pageable pageable);

    // 별점 낮은 순
    Page<DestinationReview> findAllByDestinationIdAndMemberNicknameOrderByRatingAsc(Long destinationId, String nickname, Pageable pageable);
    Page<DestinationReview> findAllByDestinationIdAndMemberNicknameNotOrderByRatingAsc(Long destinationId, String nickname, Pageable pageable);

    // 최신순
    Page<DestinationReview> findAllByDestinationIdAndMemberNicknameOrderByCreatedAtDesc(Long destinationId, String nickname, Pageable pageable);
    Page<DestinationReview> findAllByDestinationIdAndMemberNicknameNotOrderByCreatedAtDesc(Long destinationId, String nickname, Pageable pageable);

    // 추천순
    Page<DestinationReview> findAllByDestinationIdAndMemberNicknameOrderByRecommendCountDesc(Long destinationId, String nickname, Pageable pageable);
    Page<DestinationReview> findAllByDestinationIdAndMemberNicknameNotOrderByRecommendCountDesc(Long destinationId, String nickname, Pageable pageable);
}
