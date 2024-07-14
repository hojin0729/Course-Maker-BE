package coursemaker.coursemaker.domain.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import coursemaker.coursemaker.domain.auth.dto.CustomMemberDetails;
import coursemaker.coursemaker.domain.auth.dto.LoginRequestDto;
import coursemaker.coursemaker.domain.auth.dto.LoginResponseDto;
import coursemaker.coursemaker.domain.auth.exception.InvalidPasswordException;
import coursemaker.coursemaker.domain.auth.jwt.JwtProvider;
import coursemaker.coursemaker.domain.member.exception.UserNotFoundException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

/*이메일 로그인*/
@Slf4j
@RequiredArgsConstructor
public class EmailLoginFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private ObjectMapper objectMapper = new ObjectMapper();

    /*로그인 필터*/
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        /*로그인 정보 가져옴*/
        try {
            LoginRequestDto dto = objectMapper.readValue(request.getInputStream(), LoginRequestDto.class);
            log.info("[AUTH] 로그인 요청: {}", dto.getLoginEmail());

            /*검증용 객체에 담음*/
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(dto.getLoginEmail(), dto.getPassword(), null);

            /*로그인 검증 진행*/
            return authenticationManager.authenticate(token);

        } catch (IOException e) {
            log.error("[AUTH] 로그인 인증 오류: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            throw new RuntimeException(e);
        }
    }
    
    /*로그인 성공시*/
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        CustomMemberDetails member = (CustomMemberDetails) authResult.getPrincipal();
        log.info("[AUTH] 로그인 성공: {}, JWT 발급 진행", authResult.getName());

        /*권한 추출*/
        Collection<? extends GrantedAuthority> authorities = authResult.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        SecurityContextHolder.getContext().setAuthentication(authResult);

        /*토큰 발급*/
        String accessToken = jwtProvider.createAccessToken(member.getUsername(), role);
        String refreshToken = jwtProvider.createRefreshToken(member.getUsername());
        log.info("[JWT] JWT 발급 완료");

        /*토큰을 반환 객체에 담아서 반환*/
        LoginResponseDto responseDto = new LoginResponseDto();
        responseDto.setAccessToken(accessToken);
        responseDto.setRefreshToken(refreshToken);

        String result = objectMapper.writeValueAsString(responseDto);
        response.getWriter().write(result);
    }
    
    /*로그인 실패시*/
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {

        log.error("[AUTH] 로그인 실패: {}", failed.getMessage());
        if(failed.getCause() instanceof UserNotFoundException) {
            throw new UserNotFoundException("존재하지 않는 회원입니다.", "Login Fail: " + failed.getMessage());
        } else{
            throw new InvalidPasswordException("비밀번호가 일치하지 않습니다.", "Login Fail: " + failed.getMessage());
        }
    }
}
