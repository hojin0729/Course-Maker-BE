package coursemaker.coursemaker.domain.course.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import coursemaker.coursemaker.domain.course.entity.TravelCourse;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TravelCourseRepository extends JpaRepository<TravelCourse, Long> {

    Page<TravelCourse> findAllByOrderByViewsDesc(Pageable pageable);
}