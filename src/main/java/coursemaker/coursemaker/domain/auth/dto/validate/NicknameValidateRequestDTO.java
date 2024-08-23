package coursemaker.coursemaker.domain.auth.dto.validate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class NicknameValidateRequestDTO {
    @NotEmpty(message = "검증할 닉네임을 입력해주세요.")
    @Schema(description = "검증할 닉네임", example = "혁진쨩")
    private String nickname;
}
