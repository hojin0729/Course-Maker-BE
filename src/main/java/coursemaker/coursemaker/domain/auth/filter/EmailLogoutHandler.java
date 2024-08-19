package coursemaker.coursemaker.domain.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import coursemaker.coursemaker.domain.auth.dto.LogoutRequestDto;
import coursemaker.coursemaker.domain.auth.jwt.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class EmailLogoutHandler implements LogoutHandler {
    private final JwtProvider jwtProvider;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        try {

            LogoutRequestDto dto = objectMapper.readValue(request.getInputStream(), LogoutRequestDto.class);
            String nickname = jwtProvider.getNickname(dto.getRefreshToken());
            log.info("[AUTH] 로그아웃: {}", nickname);
            jwtProvider.expireRefreshToken(dto.getRefreshToken());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
