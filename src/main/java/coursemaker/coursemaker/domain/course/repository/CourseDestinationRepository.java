package coursemaker.coursemaker.domain.course.repository;

import coursemaker.coursemaker.domain.course.entity.CourseDestination;
import coursemaker.coursemaker.domain.destination.entity.Destination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface CourseDestinationRepository extends JpaRepository<CourseDestination, Long> {
}