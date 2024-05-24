package coursemaker.coursemaker.domain.course.dto;

import coursemaker.coursemaker.domain.course.entity.TravelCourse;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TravelCourseResponse {

    private final String title;
    private final String content;
    private final LocalDateTime createdAt;
    private final int duration;
    private final int travelerCount;
    private final int travelType;
//    private final Member member;

    public TravelCourseResponse(TravelCourse travelCourse) {
        this.title = travelCourse.getTitle();
        this.content = travelCourse.getContent();
        this.createdAt = travelCourse.getCreatedAt();
        this.duration = travelCourse.getDuration();
        this.travelerCount = travelCourse.getTravelerCount();
        this.travelType = travelCourse.getTravelType();
//        this.member = travelCourse.getMember();
    }
}
