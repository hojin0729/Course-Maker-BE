package coursemaker.coursemaker.domain.course.dto;

import coursemaker.coursemaker.domain.course.entity.TravelCourse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class UpdateTravelCourseRequest {
    private String title;
    private String content;

    @Min(value = 1, message = "Duration should not be less than 1")
    private Integer duration;

    @Min(value = 1, message = "Traveler count should not be less than 1")
    private Integer travelerCount;

    @Min(value = 0, message = "Travel type should not be negative")
    private Integer travelType;

    @NotBlank(message = "Picture link is mandatory")

    private String pictureLink;
    private List<UpdateCourseDestinationRequest> courseDestinations;

    public TravelCourse toEntity() {
        TravelCourse travelCourse = TravelCourse.builder()
                .title(title)
                .content(content)
                .duration(duration)
                .travelerCount(travelerCount)
                .travelType(travelType)
                .pictureLink(pictureLink)
                .build();

        for (UpdateCourseDestinationRequest courseDestination : courseDestinations) {
            travelCourse.updateCourseDestination(courseDestination.toEntity());
        }
        return travelCourse;
    }
}