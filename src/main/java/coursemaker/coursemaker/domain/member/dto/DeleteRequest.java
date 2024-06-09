package coursemaker.coursemaker.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DeleteRequest {
    @Schema(description = "유저 ID", example = "1")
    @NotNull(message = "유저 ID가 비어있습니다.")
    private Long userId; //TODO: 쿠키에서 가져오기
}

