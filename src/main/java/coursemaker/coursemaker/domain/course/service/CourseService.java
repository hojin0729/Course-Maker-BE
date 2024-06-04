package coursemaker.coursemaker.domain.course.service;

import coursemaker.coursemaker.domain.course.dto.AddCourseDestinationRequest;
import coursemaker.coursemaker.domain.course.dto.UpdateCourseDestinationRequest;
import coursemaker.coursemaker.domain.course.entity.CourseDestination;
import coursemaker.coursemaker.domain.course.dto.AddTravelCourseRequest;
import coursemaker.coursemaker.domain.course.dto.UpdateTravelCourseRequest;
import coursemaker.coursemaker.domain.course.entity.TravelCourse;
import coursemaker.coursemaker.domain.course.dto.CourseDestinationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CourseService {

    TravelCourse save(AddTravelCourseRequest request);
    List<TravelCourse> findAll();
    List<TravelCourse> getAllOrderByViewsDesc(Pageable pageable);
    TravelCourse findById(Long id);
    TravelCourse update(Long id, AddTravelCourseRequest request);
    void delete(Long id);
    TravelCourse incrementViews(Long id);

}