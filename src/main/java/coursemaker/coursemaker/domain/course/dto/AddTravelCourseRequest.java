package coursemaker.coursemaker.domain.course.dto;


import coursemaker.coursemaker.domain.tag.dto.TagResponseDto;
import jakarta.validation.constraints.*;
import lombok.*;
import java.util.List;


@Data
public class AddTravelCourseRequest {

    private String title;
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
    private List<AddCourseDestinationRequest> courseDestinations;

    @NotNull(message = "Member is mandatory")
    private String nickname;// 유저 닉네임
    
    // 태그를 빼먹어여?! 대가리 박고있죠ㅇㅇ
    private List<TagResponseDto> tags;

}