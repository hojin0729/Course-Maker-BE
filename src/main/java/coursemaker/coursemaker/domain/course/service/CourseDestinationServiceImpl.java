package coursemaker.coursemaker.domain.course.service;

import coursemaker.coursemaker.domain.course.dto.AddCourseDestinationRequest;
import coursemaker.coursemaker.domain.course.dto.UpdateCourseDestinationRequest;
import coursemaker.coursemaker.domain.course.entity.CourseDestination;
import coursemaker.coursemaker.domain.course.repository.CourseDestinationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseDestinationServiceImpl implements CourseDestinationService{

    @Autowired
    private final CourseDestinationRepository courseDestinationRepository;

    public CourseDestination save(AddCourseDestinationRequest request) {
        return courseDestinationRepository.save(request.toEntity());
    }

    public List<CourseDestination> findAll() {
        return courseDestinationRepository.findAll();
    }

    public CourseDestination findById(long id) {
        return courseDestinationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found: " + id));
    }

    public void delete(long id) {
        courseDestinationRepository.deleteById(id);
    }

    @Transactional
    public CourseDestination update(long id, UpdateCourseDestinationRequest request) {
        CourseDestination courseDestination = courseDestinationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found: " + id));
        courseDestination.update(request.getVisitOrder(), request.getTravelCourse()/*, request.getDestination()*/);

        return courseDestination;
    }
}
