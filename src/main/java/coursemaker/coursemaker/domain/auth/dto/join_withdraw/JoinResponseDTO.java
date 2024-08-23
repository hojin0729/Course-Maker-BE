package coursemaker.coursemaker.domain.auth.dto.join_withdraw;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class JoinResponseDTO {

    @Schema(description = "회원가입한 유저 닉네임", example = "nickname1")
    private String nickname;
}
