package coursemaker.coursemaker.domain.auth.filter;

import coursemaker.coursemaker.domain.auth.dto.CustomMemberDetails;
import coursemaker.coursemaker.domain.auth.jwt.JwtProvider;
import coursemaker.coursemaker.domain.member.entity.Member;
import coursemaker.coursemaker.domain.member.entity.Role;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authorization = request.getHeader("Authorization");
        
        /*토큰이 있는지 검증함*/
        if(authorization == null || !authorization.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        log.info("[JWT] 토큰이 존재합니다. 토큰을 검증합니다.");

        String token = authorization.split(" ")[1];

        /*토큰 만료 여부 확인*/
        if(jwtProvider.isExpired(token)) {
            log.info("[JWT] 토큰이 만료됬습니다.");
            filterChain.doFilter(request, response);
            return;
        }

        String nickname = jwtProvider.getNickname(token);
        Role roles = Role.valueOf(jwtProvider.getRoles(token));
        Member member = new Member();

        member.setNickname(nickname);
        member.setRoles(roles);
        CustomMemberDetails customMemberDetails = new CustomMemberDetails(member);
        Authentication authToken = new UsernamePasswordAuthenticationToken(member, null, customMemberDetails.getAuthorities());

        log.info("[JWT] 토큰 검증 성공. 사용자: {}", nickname);

        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}
