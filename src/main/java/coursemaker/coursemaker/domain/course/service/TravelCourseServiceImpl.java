package coursemaker.coursemaker.domain.course.service;

import coursemaker.coursemaker.domain.course.dto.AddTravelCourseRequest;
import coursemaker.coursemaker.domain.course.dto.UpdateTravelCourseRequest;
import coursemaker.coursemaker.domain.course.entity.TravelCourse;
import coursemaker.coursemaker.domain.course.repository.TravelCourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TravelCourseServiceImpl implements TravelCourseService{

    @Autowired
    public final TravelCourseRepository travelCourseRepository;
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

    public TravelCourse update(long id, UpdateTravelCourseRequest request) {
        TravelCourse travelCourse = travelCourseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found: " + id));
        travelCourse.update(request.getTitle(), request.getDescription(), request.getCreatedAt()/*, request.getMember()*/);

        return travelCourse;
    }
}
