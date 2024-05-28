package coursemaker.coursemaker.domain.course.dto;

import coursemaker.coursemaker.domain.course.entity.CourseDestination;
import coursemaker.coursemaker.domain.course.entity.TravelCourse;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class TravelCourseResponse {

    private final String title;
    private final String content;
    private final int views;
    private final int duration;
    private final int travelerCount;
    private final int travelType;
    private final String pictureLink;
    private final List<CourseDestinationResponse> courseDestinations;
//    private final Member member;

    public TravelCourseResponse(TravelCourse travelCourse) {
        this.title = travelCourse.getTitle();
        this.content = travelCourse.getContent();
        this.views = travelCourse.getViews();
        this.duration = travelCourse.getDuration();
        this.travelerCount = travelCourse.getTravelerCount();
        this.travelType = travelCourse.getTravelType();
        this.pictureLink = travelCourse.getPictureLink();
        this.courseDestinations = travelCourse.getCourseDestinations().stream()
                .map(CourseDestinationResponse::new)
                .collect(Collectors.toList());
//        this.member = travelCourse.getMember();
    }
}