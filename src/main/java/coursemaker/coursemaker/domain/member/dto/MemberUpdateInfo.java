package coursemaker.coursemaker.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class MemberUpdateInfo {

	@Schema(description = "이름", example = "홍길동")
	@NotNull(message = "이름을 입력하세요.")
	@NotBlank(message = "이름은 공백 혹은 빈 문자는 허용하지 않습니다.")
	@Pattern(regexp = "[가-힣]{2,10}", message = "이름 한글로 구성되어야 합니다.")
	private String newName;

	@Schema(description = "전화번호", example = "010-1234-5678")
	@NotNull(message = "전화번호를 입력하세요.")
	@NotBlank(message = "전화번호는 공백 혹은 빈 문자는 허용하지 않습니다.")
	@Pattern(regexp = "\\d{3}-\\d{3,4}-\\d{4}", message = "유효하지 않은 전화번호 형식입니다.")
	private String newPhoneNumber;
}
