package coursemaker.coursemaker.domain.wish.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DestinationWishResponseDto {

    @Schema(description = "목적지찜의 ID", example = "1")
    private Long id;

    @Schema(description = "목적지의 ID", example = "1")
    private Long destinationId;

    @Schema(description = "목적지의 이름", example = "Course Title1")
    private String destinationName;


    @Schema(description = "사용자의 닉네임", example = "Nickname")
    private String memberNickname;

    public DestinationWishResponseDto(Long id, Long destinationId, String destinationName, String memberNickname) {
        this.id = id;
        this.destinationId = destinationId;
        this.destinationName = destinationName;
        this.memberNickname = memberNickname;
    }
}