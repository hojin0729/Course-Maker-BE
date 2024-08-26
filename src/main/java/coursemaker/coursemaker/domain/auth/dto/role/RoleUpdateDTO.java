package coursemaker.coursemaker.domain.auth.dto.role;

import coursemaker.coursemaker.domain.member.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RoleUpdateDTO {

    @NotBlank(message = "닉네임은 비어있을 수 없습니다.")
    @Schema(description = "사용자의 닉네임을 입력합니다.", example = "혁진쨩")
    private String nickname;

    @NotBlank(message = "권한은 비어있을 수 없습니다.")
    @Schema(description = "업데이트할 권한을 입력합니다.", defaultValue = "ROLE_BEGINNER_TRAVELER", example = "ROLE_INTERMEDIATE_TRAVELER")
    private Role role;
}
