package coursemaker.coursemaker.api.tourApi.repository;

import coursemaker.coursemaker.api.tourApi.entity.TourApi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TourApiRepository extends JpaRepository<TourApi, Long> {
    Optional<TourApi> findByContentid(long contentid);
}