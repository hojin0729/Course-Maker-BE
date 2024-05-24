package coursemaker.coursemaker.domain.course.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import coursemaker.coursemaker.domain.course.entity.TravelCourse;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AddTravelCourseRequest {

    private String title;
    private String content;
    private int duration;
    private int travelerCount;
    private int travelType;
//    private Member member;

    public TravelCourse toEntity() {
        return TravelCourse.builder()
                .title(title)
                .content(content)
                .duration(duration)
                .travelerCount(travelerCount)
                .travelType(travelType)
                /*.member(member)*/
                .build();
    }
}
