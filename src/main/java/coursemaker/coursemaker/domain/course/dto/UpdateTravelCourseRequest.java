package coursemaker.coursemaker.domain.course.dto;

import coursemaker.coursemaker.domain.course.entity.CourseDestination;
import coursemaker.coursemaker.domain.course.dto.UpdateCourseDestinationRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UpdateTravelCourseRequest {
    private String title;
    private String content;
    private int duration;
    private int travelerCount;
    private int travelType;
    private String pictureLink;
    private List<CourseDestination> courseDestinations;
//    private Member member;
}