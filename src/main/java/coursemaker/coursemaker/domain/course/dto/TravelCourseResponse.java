package coursemaker.coursemaker.domain.course.dto;

import coursemaker.coursemaker.domain.course.entity.TravelCourse;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TravelCourseResponse {

    private final String title;
    private final String description;
    private final LocalDateTime createdAt;
//    private final Member member;

    public TravelCourseResponse(TravelCourse travelCourse) {
        this.title = travelCourse.getTitle();
        this.description = travelCourse.getDescription();
        this.createdAt = travelCourse.getCreatedAt();
//        this.member = travelCourse.getMember();
    }
}
