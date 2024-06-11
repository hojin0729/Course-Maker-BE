package coursemaker.coursemaker.jwt;

import coursemaker.coursemaker.domain.member.entity.Member;
import coursemaker.coursemaker.domain.member.service.MemberService;
import coursemaker.coursemaker.jwt.exception.InvalidTokenException;
import coursemaker.coursemaker.oauth.KakaoToken;
import coursemaker.coursemaker.oauth.KakaoTokenService;
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
    private final MemberService memberService;
    private final KakaoTokenService kakaoTokenService;


    public boolean refreshAuthentication(HttpServletRequest request, HttpServletResponse response) {
        String accessToken = getTokenFromRequest(request);
        RefreshToken refreshToken = refreshTokenRepository.findByAccessToken(accessToken)
                .orElseThrow();
        Member loginUser = memberService.findById(Long.valueOf(refreshToken.getId()));

        log.info("Refresh Token: {}", refreshToken.getRefreshToken());

        if (jwtTokenProvider.validateToken(refreshToken.getRefreshToken(), request)) {
            log.info("[refreshToken] 기존 RefreshToken으로 AccessToken 재발급 && 새 RefreshToken 발급 시작");
            String newAccessToken = jwtTokenProvider.createAccessToken(String.valueOf(Long.valueOf(refreshToken.getId())));
            String newRefreshToken = jwtTokenProvider.createRefreshToken();
            int remainingTTL = (int) jwtTokenProvider.getRemainingTTL(refreshToken.getRefreshToken());

            refreshTokenService.saveTokenInfo(loginUser.getId(), newRefreshToken, newAccessToken, remainingTTL);

            if(loginUser.getLoginType().toString().equals("KAKAO")){ //카카오로 로그인
                KakaoToken kakaoToken = kakaoTokenService.getByAccessToken(accessToken)
                        .orElseThrow();

                kakaoTokenService.saveKakaoTokenInfo(loginUser.getId(), kakaoToken.getKakaoToken(), newAccessToken);
            }
            log.info("[refreshToken] AccessToken이 재발급 되었습니다: {}", newAccessToken);
            log.info("[refreshToken] RefreshToken이 재발급 되었습니다: {}", newRefreshToken);

            response.addHeader("Authorization", "Bearer " + newAccessToken);
            return true;
        } else {
            log.warn("[refreshToken] RefreshToken이 만료 되었습니다.");
            return false;
        }

    }

    public String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if(bearerToken == null) {
            return null;
        }
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            log.info("BearerToken: {}", bearerToken);
            return bearerToken.substring("Bearer ".length());
        }else {
            log.error("Invalid token format: {}", bearerToken);
            throw new InvalidTokenException("토큰 형식이 잘못되었습니다.", "invalid token");
        }

    }

//    public Boolean isBlack(String accessToken) {
//        return jwtTokenProvider.isBlackList(accessToken);
//    }
}
