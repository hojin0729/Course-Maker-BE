package coursemaker.coursemaker.domain.course.dto;

import coursemaker.coursemaker.domain.course.entity.CourseDestination;
import coursemaker.coursemaker.domain.course.dto.UpdateCourseDestinationRequest;
import coursemaker.coursemaker.domain.course.entity.TravelCourse;
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
    private List<UpdateCourseDestinationRequest> courseDestinations;
//    private Member member;

    public TravelCourse toEntity() {
        TravelCourse travelCourse = TravelCourse.builder()
                .title(title)
                .content(content)
                .duration(duration)
                .travelerCount(travelerCount)
                .travelType(travelType)
                .pictureLink(pictureLink)
                /*.member(member)*/
                .build();

        for (UpdateCourseDestinationRequest courseDestination : courseDestinations) {
            travelCourse.updateCourseDestination(courseDestination.toEntity());
        }
        return travelCourse;
    }
}