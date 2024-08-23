package coursemaker.coursemaker.domain.auth.dto.login_logout;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoginRequestDTO {

    @Schema(description = "이메일", example = "user@example.com")
    @NotNull(message = "이메일을 입력하세요.")
    @NotBlank(message = "이메일은 공백 혹은 빈 문자는 혀용하지 않습니다.")
    private String loginEmail;

    @Schema(description = "비밀번호", example = "your_password")
    @NotNull(message = "비밀번호를 입력하세요.")
    @NotBlank(message = "비밀번호는 공백 혹은 빈 문자는 허용하지 않습니다.")
    private String password;
}
