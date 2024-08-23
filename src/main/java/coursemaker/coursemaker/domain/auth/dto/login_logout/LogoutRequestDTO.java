package coursemaker.coursemaker.domain.auth.dto.login_logout;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class LogoutRequestDTO {

    @Schema(description = "로그아웃시 삭제할 리프레시 토큰", example = "eyJraWQiOiJyZWZyZXNoIiwiYWxnIjoiSFMyNTYifQ.eyJzdWIiOiJuaWNrbmFtZTEiLCJyb2xlIjoiVVNFUiIsImlzcyI6IkNvdXJzZU1ha2VyIiwiaWF0IjoxNzI0MDcwMTgzLCJleHAiOjE3MjQxNTY1ODN9.Kq3IxNvjKXr5IUunCWraKVisqBr37E03t8VtBLFUhqU")
    private String refreshToken;
}