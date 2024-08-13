package coursemaker.coursemaker.domain.wish.dto;

import coursemaker.coursemaker.domain.wish.entity.DestinationWish;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class DestinationWishRequestDto {

    @Schema(description = "목적지의 Id", example = "1")
    @NotNull(message = "목적지 ID를 입력해주세요.")
    private Long destinationId;

    @Schema(description = "사용자의 Id", example = "1")
    @NotNull(message = "사용자 ID를 입력해주세요.")
    private Long memberId;

    public DestinationWishRequestDto(Long destinationId, Long memberId) {
        this.destinationId = destinationId;
        this.memberId = memberId;
    }
}
