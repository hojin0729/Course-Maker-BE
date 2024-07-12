package coursemaker.coursemaker.domain.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import coursemaker.coursemaker.exception.ErrorResponse;
import coursemaker.coursemaker.exception.RootException;
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
