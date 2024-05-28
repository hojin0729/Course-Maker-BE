package coursemaker.coursemaker.domain.course.service;

import coursemaker.coursemaker.domain.course.dto.AddCourseDestinationRequest;
import coursemaker.coursemaker.domain.course.dto.UpdateCourseDestinationRequest;
import coursemaker.coursemaker.domain.course.entity.CourseDestination;
import coursemaker.coursemaker.domain.course.dto.AddTravelCourseRequest;
import coursemaker.coursemaker.domain.course.dto.UpdateTravelCourseRequest;
import coursemaker.coursemaker.domain.course.entity.TravelCourse;
import coursemaker.coursemaker.domain.destination.entity.Destination;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public interface CourseService {

    TravelCourse save(AddTravelCourseRequest request);
    List<TravelCourse> findAll();
    TravelCourse findById(long id);
    TravelCourse update(long id, UpdateTravelCourseRequest request);
    void delete(long id);
    CourseDestination addCourseDestination(AddCourseDestinationRequest request);
    CourseDestination updateCourseDestination(long id, UpdateCourseDestinationRequest request);
    void deleteCourseDestination(long id);
    Page<TravelCourse> findAllOrderByViewsDesc(Pageable pageable);
    TravelCourse incrementViews(long id);

}