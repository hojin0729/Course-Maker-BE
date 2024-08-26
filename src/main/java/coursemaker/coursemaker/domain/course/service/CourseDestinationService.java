package coursemaker.coursemaker.domain.course.service;

import coursemaker.coursemaker.domain.auth.dto.LoginedInfo;
import coursemaker.coursemaker.domain.course.dto.CourseDestinationResponse;
import coursemaker.coursemaker.domain.course.entity.CourseDestination;
import coursemaker.coursemaker.domain.course.entity.TravelCourse;
import coursemaker.coursemaker.domain.course.repository.CourseDestinationRepository;
import coursemaker.coursemaker.domain.destination.dto.DestinationDto;
import coursemaker.coursemaker.domain.destination.entity.Destination;
import coursemaker.coursemaker.domain.review.service.DestinationReviewService;
import coursemaker.coursemaker.domain.tag.dto.TagResponseDto;
import coursemaker.coursemaker.domain.tag.service.TagService;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CourseDestinationService {

    private final TagService tagService;
    private final CourseDestinationRepository courseDestinationRepository;
    private final DestinationReviewService destinationReviewService;

    public CourseDestinationService(@Lazy TagService tagService,
                                    CourseDestinationRepository courseDestinationRepository,
                                    DestinationReviewService destinationReviewService) {
        this.tagService = tagService;
        this.courseDestinationRepository = courseDestinationRepository;
        this.destinationReviewService = destinationReviewService;
    }

    public CourseDestinationResponse toResponse(CourseDestination courseDestination, @AuthenticationPrincipal LoginedInfo loginedInfo) {
        List<TagResponseDto> tags = tagService.findAllByDestinationId(courseDestination.getDestination().getId());
        Double averageRating = destinationReviewService.getAverageRating(courseDestination.getDestination().getId());
        Destination destination = courseDestination.getDestination();
        boolean isApiData = destination.getIsApiData();

        boolean isMine = loginedInfo != null &&
                loginedInfo.getNickname().equals(courseDestination.getDestination().getMember().getNickname());

        DestinationDto destinationDto = DestinationDto.toDto(courseDestination.getDestination(), tags, isApiData, averageRating, isMine);
        return new CourseDestinationResponse(courseDestination, destinationDto);
    }

    public List<CourseDestination> getCourseDestinations(TravelCourse course) {
        return courseDestinationRepository.findAllByTravelCourse(course);
    }
}