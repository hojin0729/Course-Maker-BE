package coursemaker.coursemaker.domain.auth.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

/*이메일 로그인*/
@RequiredArgsConstructor
@Slf4j
public class EmailLoginFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    
    /*로그인 필터*/
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        
        /*로그인 정보 가져옴*/
        // TODO: JWT로 사용자 정보 가져오기
        String username=obtainUsername(request);
        String password=obtainPassword(request);
        
        /*검증용 객체에 담음*/
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, password, null);
        
        /*로그인 검증 진행*/
        return authenticationManager.authenticate(authentication);
    }
    
    /*로그인 성공시*/
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        log.info("로그인 성공: {}", authResult.getName());
    }
    
    /*로그인 실패시*/
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        log.info("로그인 실패: {}", failed.getMessage());
    }
}
