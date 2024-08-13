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
    private String destinationName;  // Destination의 이름이 있다고 가정

    @Schema(description = "사용자의 ID", example = "해운대")
    private Long memberId;

    @Schema(description = "사용자의 닉네임", example = "Nickname")
    private String memberNickname;  // Member의 닉네임이 있다고 가정

    public DestinationWishResponseDto(Long id, Long destinationId, String destinationName, Long memberId, String memberNickname) {
        this.id = id;
        this.destinationId = destinationId;
        this.destinationName = destinationName;
        this.memberId = memberId;
        this.memberNickname = memberNickname;
    }
}