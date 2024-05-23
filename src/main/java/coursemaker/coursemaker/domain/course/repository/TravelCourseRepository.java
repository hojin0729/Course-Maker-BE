package coursemaker.coursemaker.domain.course.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import coursemaker.coursemaker.domain.course.entity.TravelCourse;

public interface TravelCourseRepository extends JpaRepository<TravelCourse, Long> {
}
