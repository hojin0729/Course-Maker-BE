package coursemaker.coursemaker.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class ValidateEmailRequestDTO {

    @Email(message = "이메일은 example@gmail.com 과 같은 이메일 형식이어야 합니다.")
    @Schema(description = "인증받을 이메일", example = "test@email.com")
    private String email;

    @Schema(description = "인증코드", example = "1q2w3e4r")
    private String code;
}
