package coursemaker.coursemaker.domain.tag.repository;

import coursemaker.coursemaker.domain.tag.entity.CourseTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseTagRepository extends JpaRepository<CourseTag, Long> {
    List<CourseTag> findAllByTagId(Long tagId);// 태그 id에 포함된 모든 코스 반환


    List<CourseTag> findAllByCourseId(Long courseId);// 해당 코스 id에 포함된 모든 태그 반환

    Optional<CourseTag> findByCourseIdAndTagId(Long courseId, Long tagId);// 해당 코스에 태그가 포함되 있는지 확인

    void deleteByCourseIdAndTagId(Long courseId, Long id);// 해해당 코스에 있는 태그를 삭제함
    void deleteAllByCourseId(Long courseId);
}
