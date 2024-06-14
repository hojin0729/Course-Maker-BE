package coursemaker.coursemaker.jwt;

import coursemaker.coursemaker.jwt.exception.InvalidTokenException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.lang.reflect.Parameter;

//JwtInterceptor 클래스는 HandlerInterceptor를 구현하여 HTTP 요청이 컨트롤러에 도달하기 전에 JWT 토큰을 검증하는 역할 수행함
@RequiredArgsConstructor
@Component
@Slf4j
public class JwtInterceptor implements HandlerInterceptor {
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtUtil jwtUtil;

    @Override //preHandle 메서드는 컨트롤러가 요청을 처리하기 전에 실행됨. JWT 토큰의 유효성을 검사하여 요청을 계속 진행할지 결정
    public boolean preHandle(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws Exception {
        log.info("@@@@@@@@@@@@인터셉터 쌈@뽕 하게 동작중@@@@@@@@@@@@@@@");

        //헤더에 JWT 토큰이 포함되어 있으면 requiresAuthorization을 true로 설정
        boolean requiresAuthorization = jwtUtil.getTokenFromRequest(request) != null;

        if(HttpMethod.OPTIONS.matches(request.getMethod())) {
            return true; //OPTIONS 요청이 들어오면 이를 바로 허용하여 CORS 정책을 준수하도록 함
        }

        if (handler instanceof HandlerMethod handlerMethod) { //handler 객체가 HandlerMethod 클래스의 인스턴스인지 확인


            if (requiresAuthorization) {
                String accessToken = jwtUtil.getTokenFromRequest(request); //요청 헤더에서 Access토큰 추출

                log.info("[preHandle] accessToken 추출 완료, token: {}", accessToken);
                log.info("[preHandle] accessToken 유효성 체크 시작");

                //추출된 Access 토큰이 유효한지 validateToken으로 검증. 토큰이 유효하면 요청을 계속 진행
                if (accessToken != null && jwtTokenProvider.validateToken(accessToken, request)) {
                    log.info("[preHandle] accessToken 유효성 체크 완료");
                    response.addHeader("Authorization", accessToken); //응답 헤더에 Access Token을 추가

                    //Access 토큰이 유효한 경우,기본 preHandle 동작을 유지하며 요청을 컨트롤러로 전달
                    return HandlerInterceptor.super.preHandle(request, response, handler);
                } else {
                    log.warn("[preHandle] AccessToken이 만료되었습니다.");

                    //토큰이 유효하지 않으면 예외를 발생시킴
                    throw new InvalidTokenException("AccessToken이 만료되었습니다.", "expired accessToken");
                }
            }
        }
        return true;
    }
}
