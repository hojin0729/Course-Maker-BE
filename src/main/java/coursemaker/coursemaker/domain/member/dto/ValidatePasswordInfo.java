package coursemaker.coursemaker.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ValidatePasswordInfo {

	@Schema(description = "비밀번호", example = "password1@")
	@NotNull(message = "비밀번호를 입력하세요.")
	@NotBlank(message = "비밀번호는 공백 혹은 빈 문자는 허용하지 않습니다.")
	@Size(min = 4, max = 15, message = "비밀번호는 최소 4자 이상, 최대 15자 이하이어야 합니다.")
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,}$", message = "비밀번호는 적어도 하나의 숫자/특수문자/소문자가 포함되어야 합니다.")
	private String password;
}
