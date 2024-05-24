package coursemaker.coursemaker.domain.course.service;

import coursemaker.coursemaker.domain.course.dto.AddTravelCourseRequest;
import coursemaker.coursemaker.domain.course.dto.UpdateTravelCourseRequest;
import coursemaker.coursemaker.domain.course.entity.TravelCourse;

import java.util.List;

public interface TravelCourseService {

    TravelCourse save(AddTravelCourseRequest request);

    List<TravelCourse> findAll();

    TravelCourse findById(long id);

    void delete(long id);

    TravelCourse update(long id, UpdateTravelCourseRequest request);

}
