package coursemaker.coursemaker.domain.course.repository;

import coursemaker.coursemaker.domain.course.entity.CourseDestination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseDestinationRepository extends JpaRepository<CourseDestination, Long> {
}