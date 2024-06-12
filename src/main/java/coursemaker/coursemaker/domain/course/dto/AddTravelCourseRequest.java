package coursemaker.coursemaker.domain.course.dto;


import coursemaker.coursemaker.domain.tag.dto.TagResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;


@Schema(description = "코스 추가 DTO")
@Data
public class AddTravelCourseRequest {

    @Schema(description = "코스 타이틀", example = "Course Title1")
    @NotBlank(message = "코스 타이틀을 입력해야 합니다.")
    private String title;

    @Schema(description = "코스 내용", example = "Course Content1")
    @NotBlank(message = "코스 내용을 입력해야 합니다.")
    private String content;

    @Schema(description = "여행 기간", example = "3")
    @NotNull(message = "여행 기간은 1 이상 3 이하의 값을 가져야 합니다.")
    @Min(value = 1, message = "여행 기간은 1 이상 3 이하의 값을 가져야 합니다.")
    @Max(value = 3, message = "여행 기간은 1 이상 3 이하의 값을 가져야 합니다.")
    private Integer duration;

    @Schema(description = "여행 인원", example = "5")
    @NotNull(message = "여행 인원은 1명 이상이어야 합니다.")
    @Min(value = 1, message = "여행 인원은 1명 이상이어야 합니다.")
    private Integer travelerCount;

    @Schema(description = "여행 타입", example = "0", nullable = true)
//    @NotNull(message = "여행 타입을 선택하셔야 합니다.")
//    @Min(value = 0, message = "여행 타입을 선택하셔야 합니다.")
    private Integer travelType = 0;

    @Schema(description = "코스 대표 이미지 주소", example = "http://example.com/course1.jpg")
    @NotBlank(message = "이미지 링크를 넣어야 합니다.")
    private String pictureLink;

    @Schema(description = "코스 여행지 목록")
    @NotNull(message = "최소한 한 개의 코스 여행지가 있어야 합니다.")
    @Size(min = 1, message = "최소한 한 개의 코스 여행지가 있어야 합니다.")
    private List<AddCourseDestinationRequest> courseDestinations;

    @Schema(description = "유저 닉네임", example = "nickname1", hidden = true)
    //@NotNull(message = "닉네임이 비어있지 않아야 합니다.")
    private String nickname;// 유저 닉네임

    // 태그를 빼먹어여?! 대가리 박고있죠ㅇㅇ
    @Schema(description = "코스 태그 목록")
    @NotNull(message = "최소한 한 개의 태그가 있어야 합니다.")
    @Size(min = 1, message = "최소한 한 개의 태그가 있어야 합니다.")
    private List<TagResponseDto> tags;

}
