package coursemaker.coursemaker.domain.wish.repository;

import coursemaker.coursemaker.domain.wish.entity.CourseWish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseWishRepository extends JpaRepository<CourseWish, Long> {
    CourseWish findByTravelCourseId(Long travelCourseId);
}
