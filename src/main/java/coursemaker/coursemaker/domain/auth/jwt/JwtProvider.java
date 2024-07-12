package coursemaker.coursemaker.domain.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
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
