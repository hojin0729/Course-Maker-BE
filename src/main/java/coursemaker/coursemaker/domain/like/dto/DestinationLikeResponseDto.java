package coursemaker.coursemaker.domain.like.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DestinationLikeResponseDto {


    @Schema(description = "목적지의 ID", example = "1")
    private Long destinationId;

    @Schema(description = "목적지의 이름", example = "Course Title1")
    private String destinationName;


    @Schema(hidden = true)
    private String memberNickname;

    public DestinationLikeResponseDto(Long destinationId, String destinationName, String memberNickname) {
        this.destinationId = destinationId;
        this.destinationName = destinationName;
        this.memberNickname = memberNickname;
    }
}