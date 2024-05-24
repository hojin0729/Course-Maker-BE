package coursemaker.coursemaker.domain.course.service;

import coursemaker.coursemaker.domain.course.dto.AddCourseDestinationRequest;
import coursemaker.coursemaker.domain.course.dto.UpdateCourseDestinationRequest;
import coursemaker.coursemaker.domain.course.entity.CourseDestination;
import coursemaker.coursemaker.domain.course.repository.CourseDestinationRepository;

import coursemaker.coursemaker.domain.course.dto.AddTravelCourseRequest;
import coursemaker.coursemaker.domain.course.dto.UpdateTravelCourseRequest;
import coursemaker.coursemaker.domain.course.entity.TravelCourse;
import coursemaker.coursemaker.domain.course.repository.TravelCourseRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService{

    @Autowired
    private final CourseDestinationRepository courseDestinationRepository;

    @Autowired
    private  final TravelCourseRepository travelCourseRepository;


    //CourseDestination

//    public CourseDestination courseDestinationSave(AddCourseDestinationRequest request) {
//        return courseDestinationRepository.save(request.toEntity());
//    }
//
//    public List<CourseDestination> courseDestinationFindAll() {
//        return courseDestinationRepository.findAll();
//    }
//
//    public CourseDestination courseDestinationFindById(long id) {
//        return courseDestinationRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("not found: " + id));
//    }
//
//    public void courseDestinationDelete(long id) {
//        courseDestinationRepository.deleteById(id);
//    }
//
//    @Transactional
//    public CourseDestination courseDestinationUpdate(long id, UpdateCourseDestinationRequest request) {
//        CourseDestination courseDestination = courseDestinationRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("not found: " + id));
//        courseDestination.update(request.getVisitOrder(), request.getTravelCourse()/*, request.getDestination()*/);
//
//        return courseDestination;
//    }

    // TravelCourse

    public TravelCourse save(AddTravelCourseRequest request) {
        return travelCourseRepository.save(request.toEntity());
    }

    public List<TravelCourse> findAll() {
        return travelCourseRepository.findAll();
    }

    public TravelCourse findById(long id) {
        return travelCourseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found: " + id));
    }

    public void delete(long id) {
        travelCourseRepository.deleteById(id);
    }

    @Transactional
    public TravelCourse update(long id, UpdateTravelCourseRequest request) {
        TravelCourse travelCourse = travelCourseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found: " + id));
        travelCourse.update(request.getTitle(), request.getContent(), request.getCreatedAt(), request.getDuration(), request.getTravelerCount(), request.getTravelType()/*, request.getMember()*/);

        return travelCourse;
    }

}
