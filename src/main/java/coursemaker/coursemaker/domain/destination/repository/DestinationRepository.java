package coursemaker.coursemaker.domain.destination.repository;

import coursemaker.coursemaker.domain.destination.entity.Destination;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DestinationRepository extends JpaRepository<Destination, Long> {
//    Page<Destination> findAll(Pageable pageable);
//    boolean existsByName(String name);
//    boolean existsByNameAndIdNot(String name, Long id);
//    // tourApi에서 DestinationDB로 저장할 때 사용됩니다.
//    Optional<Destination> findByContentId(Long contentid);
//    // busanApi에서 DestinationDB로 저장할 때 사용됩니다.
//    Optional<Destination> findBySeq(int seq);
//    // 제목에 특정 문자열이 포함된 여행지를 검색하는 메서드
//    Page<Destination> findByNameContainingAndDeletedAtIsNull(String name, Pageable pageable);
//    // 닉네임으로 여행지를 검색하는 메서드
//    Page<Destination> findByMemberNicknameAndDeletedAtIsNull(String nickname, Pageable pageable);

    // 소프트 딜리트된 엔티티를 제외한 전체 목록 조회
    Page<Destination> findAllByDeletedAtIsNull(Pageable pageable);

    // 소프트 딜리트된 엔티티를 제외한 전체 목록 조회
    List<Destination> findAllByDeletedAtIsNull();

    // 소프트 딜리트된 항목을 제외하고 ID로 조회
    Optional<Destination> findByIdAndDeletedAtIsNull(Long id);

    // 소프트 딜리트된 엔티티를 제외한 이름 중복 체크
    boolean existsByNameAndDeletedAtIsNull(String name);

    // 소프트 딜리트된 엔티티를 제외한 다른 ID와의 이름 중복 체크
    boolean existsByNameAndIdNotAndDeletedAtIsNull(String name, Long id);

    // tourApi에서 DestinationDB로 저장할 때 사용됩니다.
    Optional<Destination> findByContentIdAndDeletedAtIsNull(Long contentid);

    // busanApi에서 DestinationDB로 저장할 때 사용됩니다.
    Optional<Destination> findBySeqAndDeletedAtIsNull(int seq);

    // 제목에 특정 문자열이 포함된 소프트 딜리트되지 않은 여행지를 검색하는 메서드
    Page<Destination> findByNameContainingAndDeletedAtIsNull(String name, Pageable pageable);

    // 소프트 딜리트되지 않은 특정 닉네임의 사용자가 생성한 여행지를 검색하는 메서드
    Page<Destination> findByMemberNicknameAndDeletedAtIsNull(String nickname, Pageable pageable);
}