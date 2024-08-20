package coursemaker.coursemaker.domain.destination.repository;

import coursemaker.coursemaker.domain.destination.entity.Destination;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DestinationRepository extends JpaRepository<Destination, Long> {
    Page<Destination> findAll(Pageable pageable);
    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, Long id);
    // tourApi에서 DestinationDB로 저장할 때 사용됩니다.
    Optional<Destination> findByContentId(Long contentid);
    // busanApi에서 DestinationDB로 저장할 때 사용됩니다.
    Optional<Destination> findBySeq(int seq);
    // 제목에 특정 문자열이 포함된 여행지를 검색하는 메서드
    Page<Destination> findByNameContainingAndDeletedAtIsNull(String name, Pageable pageable);
    // 닉네임으로 여행지를 검색하는 메서드
    Page<Destination> findByMemberNicknameAndDeletedAtIsNull(String nickname, Pageable pageable);
}