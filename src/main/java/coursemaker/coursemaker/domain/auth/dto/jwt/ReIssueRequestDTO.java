package coursemaker.coursemaker.domain.auth.dto.jwt;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ReIssueRequestDTO {

    @Schema(description = "다시 로그인 요청할 리프레시 토큰", example = "eyJraWQiOiJyZWZyZXNoIiwiYWxnIjoiSFMyNTYifQ.eyJzdWIiOiJuaWNrbmFtZTEiLCJyb2xlIjoiVVNFUiIsImlzcyI6IkNvdXJzZU1ha2VyIiwiaWF0IjoxNzI0MDcwMTgzLCJleHAiOjE3MjQxNTY1ODN9.Kq3IxNvjKXr5IUunCWraKVisqBr37E03t8VtBLFUhqU")
    private String refreshToken;
}
