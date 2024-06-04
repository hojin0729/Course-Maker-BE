package coursemaker.coursemaker.domain.course.service;

import coursemaker.coursemaker.domain.course.dto.*;
import coursemaker.coursemaker.domain.course.entity.CourseDestination;
import coursemaker.coursemaker.domain.course.exception.IllegalTravelCourseArgumentException;
import coursemaker.coursemaker.domain.course.exception.TravelCourseDuplicatedException;
import coursemaker.coursemaker.domain.course.exception.TravelCourseNotFoundException;
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
import java.util.Optional;
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
        Optional<TravelCourse> existingCourse = travelCourseRepository.findByTitle(request.getTitle());
        if (request.getTitle() == null || request.getTitle().isEmpty()) {
            throw new IllegalTravelCourseArgumentException("코스 이름이 존재하지 않습니다.", "");
        }

        if (existingCourse.isPresent()) {
            throw new TravelCourseDuplicatedException("이미 존재하는 코스입니다. ", "코스 이름: " + request.getTitle());
        }

        if (request.getContent() == null || request.getContent().isEmpty()) {
            throw new IllegalTravelCourseArgumentException("코스 내용이 존재하지 않습니다.", "");
        }

        if (request.getDuration() == null || request.getDuration() == 0) {
            throw new IllegalTravelCourseArgumentException("여행 기간이 존재하지 않습니다.", "");
        }

        if (request.getDuration() > 3) {
            throw new IllegalTravelCourseArgumentException("여행 기간은 최대 3일입니다.", "");
        }

        if (request.getTravelerCount() == null || request.getTravelerCount() == 0) {
            throw new IllegalTravelCourseArgumentException("여행 인원이 존재하지 않습니다.", "");
        }

        if (request.getTravelType() == null) {
            throw new IllegalTravelCourseArgumentException("여행 타입이 존재하지 않습니다.", "");
        }

        if (request.getPictureLink() == null || request.getPictureLink().isEmpty()) {
            throw new IllegalTravelCourseArgumentException("이미지 링크가 존재하지 않습니다.", "");
        }

        if (request.getCourseDestinations() == null || request.getCourseDestinations().isEmpty()) {
            throw new IllegalTravelCourseArgumentException("코스 여행지가 존재하지 않습니다.", "");
        }

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
                .orElseThrow(() -> new TravelCourseNotFoundException("존재하지 않는 코스입니다. ", "Course ID: " + id));
    }

    @Override
    public TravelCourse update(long id, UpdateTravelCourseRequest request) {
        TravelCourse travelCourse = travelCourseRepository.findById(id)
                .orElseThrow(() -> new TravelCourseNotFoundException("수정할 코스가 존재하지 않습니다. ", "Course ID: " + id));
        travelCourse.update(request.getTitle(), request.getContent(), request.getDuration(), request.getTravelerCount(), request.getTravelType(), request.getPictureLink());
        return travelCourseRepository.save(travelCourse);
    }

    @Override
    public void delete(long id) {
        if (!travelCourseRepository.existsById(id)) {
            throw new TravelCourseNotFoundException("삭제할 코스가 존재하지 않습니다. " + id, "Course ID: " + id);
        }
        travelCourseRepository.deleteById(id);
    }

    @Override
    public TravelCourse incrementViews(long id) {
        TravelCourse travelCourse = travelCourseRepository.findById(id)
                .orElseThrow(() -> new TravelCourseNotFoundException("코스가 존재하지 않습니다.: ", "Course ID: " + id));
        travelCourse.incrementViews();
        return travelCourseRepository.save(travelCourse);
    }
}