package coursemaker.coursemaker.domain.course.dto;

import coursemaker.coursemaker.domain.course.entity.CourseDestination;
import coursemaker.coursemaker.domain.destination.dto.DestinationDto;
import coursemaker.coursemaker.domain.destination.entity.Destination;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Schema(description = "코스 여행지 추가 DTO")
@Data
public class AddCourseDestinationRequest {

    @Schema(description = "방문 순서", example = "1")
    @NotNull(message = "방문 순서가 있어야 합니다.")
    private Short visitOrder;

    @Schema(description = "여행 일차 ex: 1일차, 2일차", example = "1")
    @NotNull(message = "여행 일차가 있어야 합니다.")
    private Short date;

    @Schema(description = "여행지")
    @NotNull(message = "코스롤 동륵할 여행지가 있어야 합니다.")
    private DestinationDto destination;


}
