package coursemaker.coursemaker.domain.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import coursemaker.coursemaker.exception.ErrorCode;
import coursemaker.coursemaker.exception.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

        log.error("[AUTH] 접근권한 거부.");
        ErrorResponse responseDTO = new ErrorResponse();
        responseDTO.setErrorType(ErrorCode.ACCESS_DENIED.getErrorType());
        responseDTO.setMessage(ErrorCode.ACCESS_DENIED.getDescription());
        responseDTO.setStatus(ErrorCode.ACCESS_DENIED.getStatus().value());

        response.setStatus(responseDTO.getStatus());
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(responseDTO));
    }
}
