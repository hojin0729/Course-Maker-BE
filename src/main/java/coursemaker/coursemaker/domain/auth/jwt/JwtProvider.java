package coursemaker.coursemaker.domain.auth.jwt;

import coursemaker.coursemaker.domain.auth.exception.InvalidTokenException;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtProvider {

    private final SecretKey secretKey;

    @Value("${jwt.access.expiration}")
    private Long accessTokenExpiration;

    @Value("${jwt.refresh.expiration}")
    private Long refreshTokenExpiration;

    public JwtProvider(@Value("${jwt.secret}") String secret) {
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),
                Jwts
                        .SIG
                        .HS256
                        .key()
                        .build()
                        .getAlgorithm()
        );
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

    public String createRefreshToken(String nickname) {

        return Jwts.builder()
                .header()
                    .keyId("refresh")
                    .and()
                .subject(nickname)// 닉네임
                .issuer("CourseMaker")
                .issuedAt(new Date(System.currentTimeMillis()) )
                .expiration(new Date(System.currentTimeMillis()+refreshTokenExpiration) )
                .signWith(secretKey)
                .compact();
    }

    public TokenType getTokenType(String token) {
        JwsHeader header = Jwts
                .parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getHeader();

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
}
