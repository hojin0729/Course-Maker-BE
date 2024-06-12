package coursemaker.coursemaker.jwt.controller;

import coursemaker.coursemaker.jwt.JwtUtil;
import coursemaker.coursemaker.jwt.dto.JwtResponse;
import coursemaker.coursemaker.jwt.exception.InvalidTokenException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "JWT", description = "JWT TOKEN API")
@RestController
@RequestMapping("/jwt")
@RequiredArgsConstructor
@Slf4j
public class JwtController {

    private final JwtUtil jwtUtil;

    @Operation(summary = "access 토큰 재발급", description = "토큰 만료 시 AccessToken 재발급")
    @PostMapping("/reissue")
    public ResponseEntity<JwtResponse> reIssueAccessToken(HttpServletRequest request, HttpServletResponse response) {
        log.info("[reIssueToken] AccessToken 재발급 시작");

        boolean isRefreshed = jwtUtil.refreshAuthentication(request, response);

        if (isRefreshed) {
            log.info("[reIssueToken] AccessToken 갱신 완료");
            JwtResponse jwtResponse = new JwtResponse();
            jwtResponse.setAccessToken(response.getHeader("Authorization"));
            return ResponseEntity.ok().body(jwtResponse);
        } else {
            log.warn("[reIssueToken] RefreshToken이 만료되었습니다. 재로그인 필요합니다.");
            throw new InvalidTokenException("RefreshToken이 만료되었습니다. 재로그인해주세요.", "expired refreshToken");
        }
    }
}
