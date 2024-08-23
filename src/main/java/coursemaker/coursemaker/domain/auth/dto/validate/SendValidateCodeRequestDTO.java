package coursemaker.coursemaker.domain.auth.dto.validate;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SendValidateCodeRequestDTO {

    @NotBlank
    @Email(message = "이메일은 example@gmail.com 과 같은 이메일 형식이어야 합니다.")
    @Schema(description = "인증 코드를 보낼 이메일", example = "sample@gmail.com")
    private String email;
}
