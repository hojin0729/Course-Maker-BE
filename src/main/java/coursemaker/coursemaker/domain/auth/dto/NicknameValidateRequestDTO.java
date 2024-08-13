package coursemaker.coursemaker.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class NicknameValidateRequestDTO {
    @NotEmpty
    @Schema(description = "검증할 닉네임", example = "혁진쨩")
    private String nickname;
}
