package coursemaker.coursemaker.domain.course.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import coursemaker.coursemaker.domain.course.entity.TravelCourse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UpdateTravelCourseRequest {
    @NotBlank(message = "Title is mandatory")
    private String title;

    @NotBlank(message = "Content is mandatory")
    private String content;

    @Min(value = 1, message = "Duration should not be less than 1")
    private int duration;

    @Min(value = 1, message = "Traveler count should not be less than 1")
    private int travelerCount;

    @Min(value = 0, message = "Travel type should not be negative")
    private int travelType;

    @NotBlank(message = "Picture link is mandatory")
    private String pictureLink;

    @NotEmpty(message = "Course destinations are mandatory")
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