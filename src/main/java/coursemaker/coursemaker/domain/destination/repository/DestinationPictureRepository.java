package coursemaker.coursemaker.domain.destination.repository;

import coursemaker.coursemaker.domain.destination.entity.DestinationPicture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DestinationPictureRepository extends JpaRepository<DestinationPicture, Long> {
}
