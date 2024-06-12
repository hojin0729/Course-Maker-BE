package coursemaker.coursemaker.jwt.dto;


import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
public class JwtResponse {
    @Schema(description = "리프레시 토큰", example = "refresh_token_example")
    private String accessToken;
}
