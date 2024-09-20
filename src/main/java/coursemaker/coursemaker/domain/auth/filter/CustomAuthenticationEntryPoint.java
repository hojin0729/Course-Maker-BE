package coursemaker.coursemaker.domain.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import coursemaker.coursemaker.exception.ErrorCode;
import coursemaker.coursemaker.exception.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.info("[AUTH] 비 로그인 사용자가 엑세스 접근");

        ErrorResponse responseDto = new ErrorResponse();
        responseDto.setStatus(ErrorCode.LOGIN_REQUIRED.getStatus().value());
        responseDto.setErrorType(ErrorCode.LOGIN_REQUIRED.getErrorType());
        responseDto.setMessage("로그인 후 이용 가능합니다.");

        response.setStatus(ErrorCode.LOGIN_REQUIRED.getStatus().value());

        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(responseDto));
    }
}
