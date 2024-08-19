package coursemaker.coursemaker.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ReIssueResponseDto {

    @Schema(description = "재발급된 access token", example = "eyJraWQiOiJyZWZyZXNoIiwiYWxnIjoiSFMyNTYifQ.eyJzdWIiOiJuaWNrbmFtZTEiLCJyb2xlIjoiVVNFUiIsImlzcyI6IkNvdXJzZU1ha2VyIiwiaWF0IjoxNzI0MDcwMTgzLCJleHAiOjE3MjQxNTY1ODN9.Kq3IxNvjKXr5IUunCWraKVisqBr37E03t8VtBLFUhqU")
    private String accessToken;
}
