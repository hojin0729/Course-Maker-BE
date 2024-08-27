package coursemaker.coursemaker.domain.wish.repository;

import coursemaker.coursemaker.domain.wish.entity.CourseWish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseWishRepository extends JpaRepository<CourseWish, Long> {
    List<CourseWish> findByMemberNickname(String nickname);

    Optional<CourseWish> findByTravelCourseIdAndMemberId(Long courseId, Long memberId);

    boolean existsByTravelCourseIdAndMemberId(Long id, Long id1);

    List<CourseWish> findByTravelCourseId(Long courseId);
}
