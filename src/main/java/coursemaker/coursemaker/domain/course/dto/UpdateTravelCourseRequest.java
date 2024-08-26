package coursemaker.coursemaker.domain.course.dto;

import coursemaker.coursemaker.domain.tag.dto.TagResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;

@Schema(description = "여행 코스 수정 DTO")
@Data
public class UpdateTravelCourseRequest {

    @Schema(description = "코스 타이틀", example = "Course Title2")
    @NotBlank(message = "코스 타이틀을 입력해야 합니다.")
    private String title;

    @Schema(description = "코스 내용", example = "Course Content2")
    @NotBlank(message = "코스 내용을 입력해야 합니다.")
    private String content;

    @Schema(description = "여행 기간", example = "2")
    @NotNull(message = "여행 기간은 1 이상 3 이하의 값을 가져야 합니다.")
    @Min(value = 1, message = "여행 기간은 1 이상 3 이하의 값을 가져야 합니다.")
    @Max(value = 3, message = "여행 기간은 1 이상 3 이하의 값을 가져야 합니다.")
    private Integer duration;

    @Schema(description = "여행 인원", example = "4")
    @NotNull(message = "여행 인원은 1명 이상이어야 합니다.")
    @Min(value = 1, message = "여행 인원은 1명 이상이어야 합니다.")
    private Integer travelerCount;

    @Schema(description = "여행 타입, 0이면 자동차, 1이면 대중교통에 해당합니다.", example = "0")
    @NotNull(message = "여행 타입을 선택하셔야 합니다.")
    @Min(value = 0, message = "여행 타입을 선택하셔야 합니다.")
    private Integer travelType;

    @Schema(description = "코스 대표 이미지 주소", example = "http://example.com/course2.jpg")
    @NotBlank(message = "이미지 링크를 넣어야 합니다.")
    private String pictureLink;

    @Schema(description = "코스 여행지 목록")
    @NotNull(message = "최소한 한 개의 코스 여행지가 있어야 합니다.")
    @Size(min = 1, message = "최소한 한 개의 코스 여행지가 있어야 합니다.")
    private List<@Valid UpdateCourseDestinationRequest> courseDestinations;

    @Schema(description = "유저 닉네임", example = "nickname2", hidden = true)
    //@NotNull(message = "닉네임이 비어있지 않아야 합니다.")
    private String nickname;// 유저 닉네임

    @Schema(description = "코스 태그 목록")
    @NotNull(message = "최소한 한 개의 태그가 있어야 합니다.")
    @Size(min = 1, message = "최소한 한 개의 태그가 있어야 합니다.")
    private List<@Valid TagResponseDto> tags;

//    public TravelCourse toEntity() {
//        TravelCourse travelCourse = TravelCourse.builder()
//                .title(title)
//                .content(content)
//                .duration(duration)
//                .travelerCount(travelerCount)
//                .travelType(travelType)
//                .pictureLink(pictureLink)
//                .build();
//
//        for (UpdateCourseDestinationRequest courseDestination : courseDestinations) {
//            travelCourse.updateCourseDestination(courseDestination.toEntity());
//        }
//        return travelCourse;
//    }
}