//package coursemaker.coursemaker.oauth.handler;
//
//import coursemaker.coursemaker.jwt.JwtTokenProvider;
//import coursemaker.coursemaker.jwt.JwtUtil;
//import coursemaker.coursemaker.oauth.dto.CustomOAuth2User;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.util.Collection;
//import java.util.Iterator;
//
//@Component
//@RequiredArgsConstructor
//public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
//
//    private final JwtUtil jwtUtil;
//    private final JwtTokenProvider jwtTokenProvider;
//
//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//
//        //OAuth2User
//        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();
//
//        String username = customUserDetails.getUsername();
//
//        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
//        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
//        GrantedAuthority auth = iterator.next();
//        String role = auth.getAuthority();
//
//        // accessToken과 refreshToken 생성
//        String accessToken = jwtTokenProvider.createAccessToken("access", username, role, 60000L);
//        String refreshToken = jwtTokenProvider.createRefreshToken("refresh", username, role, 86400000L);
//
//        // 응답
//        response.setHeader("access", "Bearer " + accessToken);
//        response.addCookie(createCookie("Authorization", refreshToken));
//        response.sendRedirect("http://localhost:3000/");
//    }
//
//    private Cookie createCookie(String key, String value) {
//
//        Cookie cookie = new Cookie(key, value);
//        cookie.setMaxAge(60*60*60);
//        //cookie.setSecure(true);
//        cookie.setPath("/");
//        cookie.setHttpOnly(true);
//
//        return cookie;
//    }
//}
