package coursemaker.coursemaker.domain.tag.repository;

import coursemaker.coursemaker.domain.tag.entity.DestinationTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DestinationTagRepository extends JpaRepository<DestinationTag, Long> {

    List<DestinationTag> findAllByTagId(Long tagId);// 태그 id에 포함된 모든 여행지 반환


    List<DestinationTag> findAllByDestinationId(Long destinationId);// 해당 여행지 id에 포함된 모든 태그 반환

    Optional<DestinationTag> findByDestinationIdAndTagId(Long destinationId, Long tagId);// 해당 여행지에 태그가 포함되 있는지 확인

    void deleteByDestinationIdAndTagId(Long destinationId, Long id);// 해당 여행지에 있는 태그를 삭제함

    void deleteAllByDestinationId(Long destinationId);

    void deleteAllByTagId(Long tagId);
}
