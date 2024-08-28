package coursemaker.coursemaker.domain.like.repository;

import coursemaker.coursemaker.domain.like.entity.DestinationLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DestinationLikeRepository extends JpaRepository<DestinationLike, Long> {
    List<DestinationLike> findByMember_Nickname(String nickname);

    Optional<DestinationLike> findByDestinationIdAndMemberId(Long destinationId, Long memberId);

    boolean existsByDestinationIdAndMemberId(Long id, Long id1);

    List<DestinationLike> findByDestinationId(Long destinationId);

    Integer countByDestinationId(Long destinationId);
}
