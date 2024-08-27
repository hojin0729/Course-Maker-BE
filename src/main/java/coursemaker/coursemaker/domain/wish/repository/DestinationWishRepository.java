package coursemaker.coursemaker.domain.wish.repository;

import coursemaker.coursemaker.domain.wish.entity.CourseWish;
import coursemaker.coursemaker.domain.wish.entity.DestinationWish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DestinationWishRepository extends JpaRepository<DestinationWish, Long> {
    List<DestinationWish> findByMember_Nickname(String nickname);

    Optional<DestinationWish> findByDestinationIdAndMemberId(Long destinationId, Long memberId);

    boolean existsByDestinationIdAndMemberId(Long id, Long id1);

    List<DestinationWish> findByDestinationId(Long destinationId);
}
