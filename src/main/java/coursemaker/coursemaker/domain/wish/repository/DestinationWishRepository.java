package coursemaker.coursemaker.domain.wish.repository;

import coursemaker.coursemaker.domain.wish.entity.DestinationWish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DestinationWishRepository extends JpaRepository<DestinationWish, Long> {
}
