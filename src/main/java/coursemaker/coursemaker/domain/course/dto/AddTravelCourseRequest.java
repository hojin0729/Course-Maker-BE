package coursemaker.coursemaker.domain.course.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import coursemaker.coursemaker.domain.course.entity.TravelCourse;
import coursemaker.coursemaker.domain.course.entity.CourseDestination;
import coursemaker.coursemaker.domain.course.dto.AddCourseDestinationRequest;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AddTravelCourseRequest {

    private String title;
    private String content;
    private int duration;
    private int travelerCount;
    private int travelType;
    private String pictureLink;
    private List<AddCourseDestinationRequest> courseDestinations;

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

        for (AddCourseDestinationRequest courseDestination : courseDestinations) {
            travelCourse.addCourseDestination(courseDestination.toEntity());
        }
        return travelCourse;
    }
}