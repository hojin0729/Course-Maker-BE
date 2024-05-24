package coursemaker.coursemaker.domain.course.service;

import coursemaker.coursemaker.domain.course.dto.AddCourseDestinationRequest;
import coursemaker.coursemaker.domain.course.dto.UpdateCourseDestinationRequest;
import coursemaker.coursemaker.domain.course.entity.CourseDestination;

import java.util.List;

public interface CourseDestinationService {
    CourseDestination save(AddCourseDestinationRequest request);

    List<CourseDestination> findAll();

    CourseDestination findById(long id);

    void delete(long id);

    CourseDestination update(long id, UpdateCourseDestinationRequest request);

}
