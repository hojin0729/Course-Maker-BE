package coursemaker.coursemaker.jwt;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Optional;

@RequiredArgsConstructor
@Component
@Slf4j
public class JwtUtil {
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenService refreshTokenService;


    public boolean refreshAuthentication(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        String accessToken = getTokenFromRequest(request);
        RefreshToken refreshToken = refreshTokenRepository.findByAccessToken(accessToken)
                .orElseThrow();

        if (jwtTokenProvider.validateToken(refreshToken.getRefreshToken())) {
            log.info("[refreshToken] 기존 RefreshToken으로 AccessToken 재발급 && 새 RefreshToken 발급 시작");
            String newAccessToken = jwtTokenProvider.createAccessToken(String.valueOf(Long.valueOf(refreshToken.getId())));
            String newRefreshToken = jwtTokenProvider.createRefreshToken();
            int remainingTTL = (int) jwtTokenProvider.getRemainingTTL(refreshToken.getRefreshToken());

            refreshTokenService.saveTokenInfo(Long.valueOf(refreshToken.getId()), newRefreshToken, newAccessToken, remainingTTL);

            log.info("[refreshToken] AccessToken이 재발급 되었습니다: {}", newAccessToken);
            log.info("[refreshToken] RefreshToken이 재발급 되었습니다: {}", newRefreshToken);
            response.addHeader("Authorization", "Bearer " + newAccessToken);
            return true;
        } else {
            log.warn("[refreshToken] RefreshToken이 만료 되었습니다.");
            return false;
        }
    } // TODO: Redis RefreshToken에 맞게 수정 2

    public String getTokenFromRequest(HttpServletRequest request) throws UnsupportedEncodingException {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            log.info("BearerToken: {}", bearerToken);
            return bearerToken.substring("Bearer ".length());
        }
        return null;
    }

//    public Boolean isBlack(String accessToken) {
//        return jwtTokenProvider.isBlackList(accessToken);
//    }
}
