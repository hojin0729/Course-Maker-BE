package coursemaker.coursemaker.domain.course.dto;

import coursemaker.coursemaker.domain.destination.dto.DestinationDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "코스 여행지 수정 DTO")
@Data
public class UpdateCourseDestinationRequest {

    @Schema(description = "방문 순서", example = "1")
    @NotNull(message = "방문 순서가 있어야 합니다.")
    private Short visitOrder;

    @Schema(description = "여행 일차 ex: 1일차, 2일차", example = "1")
    @NotNull(message = "여행 일차가 있어야 합니다.")
    private Short date;

    @Schema(description = "여행지")
    @NotNull(message = "코스롤 동륵할 여행지가 있어야 합니다.")
    @Valid
    private DestinationDto destination;

//    public CourseDestination toEntity() {
//        return CourseDestination.builder()
//                .visitOrder(visitOrder)
//                .date(date)
//                .destination(destination)
//                .build();
//    }
}