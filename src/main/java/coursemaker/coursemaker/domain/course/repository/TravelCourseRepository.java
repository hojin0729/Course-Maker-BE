package coursemaker.coursemaker.domain.course.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import coursemaker.coursemaker.domain.course.entity.TravelCourse;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TravelCourseRepository extends JpaRepository<TravelCourse, Long> {
    Page<TravelCourse> findAllByDeletedAtIsNull(Pageable pageable);
    Page<TravelCourse> findAllByDeletedAtIsNullOrderByViewsDesc(Pageable pageable);
    Optional<TravelCourse> findByIdAndDeletedAtIsNull(Long id);
    // 제목에 특정 문자열이 포함된 코스를 검색하는 메서드
    Page<TravelCourse> findByTitleContainingAndDeletedAtIsNull(String title, Pageable pageable);
    // 닉네임으로 코스를 검색하는 메서드
    Page<TravelCourse> findByMemberNicknameAndDeletedAtIsNull(String nickname, Pageable pageable);

}