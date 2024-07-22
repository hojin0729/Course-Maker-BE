package coursemaker.coursemaker.api.busanApi.repository;

import coursemaker.coursemaker.api.busanApi.entity.BusanApi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BusanApiRepository extends JpaRepository<BusanApi, Long> {
    Optional<BusanApi> findBySeq(int seq);
}