package coursemaker.coursemaker.domain.course.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UpdateTravelCourseRequest {
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private int duration;
    private int travelerCount;
    private int travelType;
//    private Member member;
}
