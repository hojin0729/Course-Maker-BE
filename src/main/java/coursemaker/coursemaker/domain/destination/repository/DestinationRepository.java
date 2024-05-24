package coursemaker.coursemaker.domain.destination.repository;

import coursemaker.coursemaker.domain.destination.entity.Destination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DestinationRepository extends JpaRepository<Destination, Long> {
}