package coursemaker.coursemaker.domain.course.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import coursemaker.coursemaker.domain.course.entity.TravelCourse;
import coursemaker.coursemaker.domain.member.entity.Member;

import java.util.List;

@Data
public class AddTravelCourseRequest {

    @NotBlank(message = "Title is mandatory")
    private String title;

    @NotBlank(message = "Content is mandatory")
    private String content;

    @Min(value = 1, message = "Duration should not be less than 1")
    @Max(value = 3)
    private Integer duration;

    @Min(value = 1, message = "Traveler count should not be less than 1")
    private Integer travelerCount;

    @Min(value = 0, message = "Travel type should not be negative")
    private Integer travelType;

    @NotBlank(message = "Picture link is mandatory")
    private String pictureLink;

    @NotEmpty(message = "Course destinations are mandatory")
    private List<AddCourseDestinationRequest> courseDestinations;

    @NotNull(message = "Member is mandatory")
    private Member member;

    public TravelCourse toEntity() {
        TravelCourse travelCourse = TravelCourse.builder()
                .title(title)
                .content(content)
                .duration(duration)
                .travelerCount(travelerCount)
                .travelType(travelType)
                .pictureLink(pictureLink)
                .member(member)
                .build();

        for (AddCourseDestinationRequest courseDestination : courseDestinations) {
            travelCourse.addCourseDestination(courseDestination.toEntity());
        }
        return travelCourse;
    }
}