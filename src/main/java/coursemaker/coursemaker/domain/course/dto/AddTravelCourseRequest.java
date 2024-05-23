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
    private String description;
    private LocalDateTime createdAt;
//    private Member member;

    public TravelCourse toEntity() {
        return TravelCourse.builder()
                .title(title)
                .description(description)
                .createdAt(createdAt)
                /*.member(member)*/
                .build();
    }
}
