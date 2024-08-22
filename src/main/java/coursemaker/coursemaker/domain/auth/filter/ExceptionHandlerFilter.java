package coursemaker.coursemaker.domain.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import coursemaker.coursemaker.domain.auth.exception.ExpiredAccessTokenException;
import coursemaker.coursemaker.exception.ErrorCode;
import coursemaker.coursemaker.exception.ErrorResponse;
import coursemaker.coursemaker.exception.RootException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Slf4j
public class ExceptionHandlerFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (RootException e) {
            returnErrorResponse(request, response, e);
        } catch (SignatureException | MalformedJwtException e) {
            RootException exception = new RootException(ErrorCode.INVALID_TOKEN, "토큰이 변조됬습니다."+e.getMessage(), "토큰이 변조됬습니다.", e);
            returnErrorResponse(request, response, exception);
        } catch (ExpiredJwtException e) {
            ExpiredAccessTokenException exception = new ExpiredAccessTokenException("토큰이 만료됬습니다.", "Access Token 만료됨.");
            returnErrorResponse(request, response, exception);
        } catch (Exception e) {
            log.error("[FILTER] 예상치 못한 오류 발생: {}", e.getMessage());
            RootException rootException = new RootException(ErrorCode.UNKNOWN_ERROR, "예상치 못한 오류 발생: "+ e.getMessage(), "예상치 못한 쌈@뽕한 오류 발생! 백엔드에게 이 메시지를 전해주세여: "+ e.getMessage(), e);
            returnErrorResponse(request, response, rootException);
        }
    }

    public void returnErrorResponse(HttpServletRequest request, HttpServletResponse response, RootException e) throws IOException {

        ErrorResponse responseDto = new ErrorResponse();
        responseDto.setStatus(e.getErrorCode().getStatus().value());
        responseDto.setErrorType(e.getErrorCode().getErrorType());
        responseDto.setMessage(e.getClientMessage());

        response.setStatus(e.getErrorCode().getStatus().value());
        ObjectMapper mapper = new ObjectMapper();
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(mapper.writeValueAsString(responseDto));


    }
}
