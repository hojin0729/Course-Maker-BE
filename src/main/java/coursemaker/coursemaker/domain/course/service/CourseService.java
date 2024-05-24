package coursemaker.coursemaker.domain.course.service;

import coursemaker.coursemaker.domain.course.dto.AddCourseDestinationRequest;
import coursemaker.coursemaker.domain.course.dto.UpdateCourseDestinationRequest;
import coursemaker.coursemaker.domain.course.entity.CourseDestination;
import coursemaker.coursemaker.domain.course.dto.AddTravelCourseRequest;
import coursemaker.coursemaker.domain.course.dto.UpdateTravelCourseRequest;
import coursemaker.coursemaker.domain.course.entity.TravelCourse;

import java.util.List;

public interface CourseService {

//    CourseDestination courseDestinationSave(AddCourseDestinationRequest request);
//
//    List<CourseDestination> courseDestinationFindAll();
//
//    CourseDestination courseDestinationFindById(long id);
//
//    void courseDestinationDelete(long id);
//
//    CourseDestination courseDestinationUpdate(long id, UpdateCourseDestinationRequest request);

    // destination 에 순서 부여




    TravelCourse save(AddTravelCourseRequest request);

    List<TravelCourse> findAll();

    TravelCourse findById(long id);

    void delete(long id);

    TravelCourse update(long id, UpdateTravelCourseRequest request);













}
