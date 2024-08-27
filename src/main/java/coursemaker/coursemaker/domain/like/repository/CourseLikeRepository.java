package coursemaker.coursemaker.domain.like.repository;

import coursemaker.coursemaker.domain.like.entity.CourseLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseLikeRepository extends JpaRepository<CourseLike, Long> {
    List<CourseLike> findByMemberNickname(String nickname);

    Optional<CourseLike> findByTravelCourseIdAndMemberId(Long courseId, Long memberId);

    boolean existsByTravelCourseIdAndMemberId(Long id, Long id1);

    List<CourseLike> findByTravelCourseId(Long courseId);

    Integer countByTravelCourseId(Long courseId);
}
