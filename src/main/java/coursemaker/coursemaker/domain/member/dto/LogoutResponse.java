package coursemaker.coursemaker.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LogoutResponse {

    @Schema(description = "성공 여부", example = "true")
    Boolean success;
}

