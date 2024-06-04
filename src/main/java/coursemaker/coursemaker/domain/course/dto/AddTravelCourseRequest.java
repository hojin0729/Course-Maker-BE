package coursemaker.coursemaker.domain.course.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import coursemaker.coursemaker.domain.course.entity.TravelCourse;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AddTravelCourseRequest {

    @NotBlank(message = "Title is mandatory")
    private String title;

    @NotBlank(message = "Content is mandatory")
    private String content;

    @Min(value = 1, message = "Duration should not be less than 1")
    @Max(value = 3)
    private int duration;

    @Min(value = 1, message = "Traveler count should not be less than 1")
    private int travelerCount;

    @Min(value = 0, message = "Travel type should not be negative")
    private int travelType;

    @NotBlank(message = "Picture link is mandatory")
    private String pictureLink;

    @NotEmpty(message = "Course destinations are mandatory")
    private List<AddCourseDestinationRequest> courseDestinations;

    public TravelCourse toEntity() {
        TravelCourse travelCourse = TravelCourse.builder()
                .title(title)
                .content(content)
                .duration(duration)
                .travelerCount(travelerCount)
                .travelType(travelType)
                .pictureLink(pictureLink)
                .build();

        for (AddCourseDestinationRequest courseDestination : courseDestinations) {
            travelCourse.addCourseDestination(courseDestination.toEntity());
        }
        return travelCourse;
    }
}