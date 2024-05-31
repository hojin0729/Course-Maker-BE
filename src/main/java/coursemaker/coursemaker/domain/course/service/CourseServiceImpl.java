package coursemaker.coursemaker.domain.course.service;

import coursemaker.coursemaker.domain.course.dto.*;
import coursemaker.coursemaker.domain.course.entity.CourseDestination;
import coursemaker.coursemaker.domain.course.repository.CourseDestinationRepository;
import coursemaker.coursemaker.domain.course.entity.TravelCourse;
import coursemaker.coursemaker.domain.course.service.CourseDestinationService;
import coursemaker.coursemaker.domain.course.repository.TravelCourseRepository;
import coursemaker.coursemaker.domain.destination.dto.DestinationDto;
import coursemaker.coursemaker.domain.tag.dto.TagResponseDto;
import coursemaker.coursemaker.domain.tag.entity.Tag;
import coursemaker.coursemaker.domain.tag.service.TagService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CourseServiceImpl implements CourseService{

    private final CourseDestinationRepository courseDestinationRepository;

    private  final TravelCourseRepository travelCourseRepository;

    @Lazy
    private final CourseDestinationService courseDestinationService;

    @Override
    public TravelCourse save(AddTravelCourseRequest request) {
        return travelCourseRepository.save(request.toEntity());
    }

    @Override
    public List<TravelCourse> findAll() {
        return travelCourseRepository.findAll();
    }

    @Override
    public Page<TravelCourse> getAllOrderByViewsDesc(Pageable pageable) {
        return travelCourseRepository.findAllByOrderByViewsDesc(pageable);
    }

    @Override
    public TravelCourse findById(long id) {
        return travelCourseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Travel course not found with id: " + id));
    }

    @Override
    public TravelCourse update(long id, UpdateTravelCourseRequest request) {
        TravelCourse travelCourse = travelCourseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Travel course not found with id: " + id));
        travelCourse.update(request.getTitle(), request.getContent(), request.getDuration(), request.getTravelerCount(), request.getTravelType(), request.getPictureLink());
        return travelCourseRepository.save(travelCourse);
    }

    @Override
    public void delete(long id) {
        travelCourseRepository.deleteById(id);
    }

//    @Override
//    public List<CourseDestinationResponse> findAllCourseDestinations() {
//        return courseDestinationRepository.findAll().stream()
//                .map(courseDestinationService::toResponse)
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public CourseDestinationResponse findCourseDestinationById(long id) {
//        CourseDestination courseDestination = courseDestinationRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("Course destination not found with id: " + id));
//        return courseDestinationService.toResponse(courseDestination);
//    }
//
//    @Override
//    public CourseDestination addCourseDestination(AddCourseDestinationRequest request) {
//        CourseDestination courseDestination = request.toEntity();
//        return courseDestinationRepository.save(courseDestination);
//    }
//
//    @Override
//    public CourseDestination updateCourseDestination(long id, UpdateCourseDestinationRequest request) {
//        CourseDestination courseDestination = courseDestinationRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("Course destination not found with id: " + id));
//        courseDestination.update(request.getVisitOrder(), request.getDate(), request.getDestination());
//        return courseDestinationRepository.save(courseDestination);
//    }
//
//    @Override
//    public void deleteCourseDestination(long id) {
//        courseDestinationRepository.deleteById(id);
//    }

    @Override
    public TravelCourse incrementViews(long id) {
        TravelCourse travelCourse = travelCourseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Travel course not found with id: " + id));
        travelCourse.incrementViews();
        return travelCourseRepository.save(travelCourse);
    }
}