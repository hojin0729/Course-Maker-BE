package coursemaker.coursemaker.domain.auth.jwt;

import coursemaker.coursemaker.domain.auth.entity.RefreshToken;
import coursemaker.coursemaker.domain.auth.exception.ExpiredTokenException;
import coursemaker.coursemaker.domain.auth.exception.InvalidTokenException;
import coursemaker.coursemaker.domain.auth.repository.RefreshTokenRepository;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtProvider {

    private final SecretKey secretKey;
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.access.expiration}")
    private Long accessTokenExpiration;

    @Value("${jwt.refresh.expiration}")
    private Long refreshTokenExpiration;

    public JwtProvider(@Value("${jwt.secret}") String secret, RefreshTokenRepository refreshTokenRepository) {
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),
                Jwts
                        .SIG
                        .HS256
                        .key()
                        .build()
                        .getAlgorithm()
        );

        this.refreshTokenRepository = refreshTokenRepository;
    }

    public String createAccessToken(String nickname, String role) {

        return Jwts.builder()
                .header()
                    .keyId("access")
                    .and()
                .subject(nickname)// 닉네임
                .claim("role", role)
                .issuer("CourseMaker")
                .issuedAt(new Date(System.currentTimeMillis()) )
                .expiration(new Date(System.currentTimeMillis()+accessTokenExpiration) )
                .signWith(secretKey)
                .compact();
    }

    public String createRefreshToken(String nickname, String role) {

        String token = Jwts.builder()
                .header()
                    .keyId("refresh")
                    .and()
                .subject(nickname)// 닉네임
                .claim("role", role)
                .issuer("CourseMaker")
                .issuedAt(new Date(System.currentTimeMillis()) )
                .expiration(new Date(System.currentTimeMillis()+refreshTokenExpiration) )
                .signWith(secretKey)
                .compact();

        saveRefreshToken(token, nickname);

        return token;
    }

    public TokenType getTokenType(String token) {
        /*헤더 추출*/
        JwsHeader header = Jwts
                .parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getHeader();

        /*헤더에서 토큰 종류 추출*/
        String keyId = header.getKeyId();
        if(keyId.equals("access")) {
            return TokenType.ACCESS_TOKEN;
        } else if(keyId.equals("refresh")) {
            return TokenType.REFRESH_TOKEN;
        } else {
            throw new InvalidTokenException("인증되지 않은 토큰 형태 입니다.", "인증되지 않은 토큰 형태: " + keyId);
        }
    }

    public Boolean isExpired(String token){
        return Jwts
                .parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration()
                .before(new Date(System.currentTimeMillis()));
    }

    public String getNickname(String token) {
        return Jwts
                .parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public String getRoles(String token) {
        return (String)Jwts
                .parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role");
    }

    public String reIssue(String refreshToken) {
        Boolean exists = refreshTokenRepository.existsByToken(refreshToken);
        Boolean expired = isExpired(refreshToken);

        if(exists && !expired) {// 토큰이 유효할때
            return createAccessToken(getNickname(refreshToken), getRoles(refreshToken));
        } else if(!exists){// 토큰이 유효하지 않을때
            throw new InvalidTokenException("인증되지 않은 토큰입니다.", "[JWT] 인증도지 않은 토큰: " + refreshToken);
        } else {// 토큰이 만료됬을때
            throw new ExpiredTokenException("토큰이 만료됬습니다.", "[JWT] refresh token 만료: " + refreshToken);
        }
    }

    /*DB에 리프레시 토큰 저장*/
    private void saveRefreshToken(String refreshToken, String nickname) {
        RefreshToken token = new RefreshToken();
        token.setToken(refreshToken);
        token.setNickname(nickname);
        token.setExpiration(refreshTokenExpiration.toString());
        refreshTokenRepository.save(token);
    }

    public void expireRefreshToken(String refreshToken) {
        refreshTokenRepository.deleteByToken(refreshToken);
    }
}
