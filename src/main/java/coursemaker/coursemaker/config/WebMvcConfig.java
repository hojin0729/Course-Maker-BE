package coursemaker.coursemaker.config;

import coursemaker.coursemaker.util.LoginUserArgumentResolver;
import coursemaker.coursemaker.jwt.JwtInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final JwtInterceptor JwtInterceptor;

    @Override
    public void addCorsMappings(final CorsRegistry registry) {
        registry.addMapping("/**")
                .exposedHeaders("Set-Cookie")
                .allowedMethods("*");
    }
    
    /*JWT 인터셉터 등록*/
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(JwtInterceptor)
                .excludePathPatterns("/v1/member")// 회원가입
                .excludePathPatterns("/v1/member/login")// 로그인
                .excludePathPatterns("/v1/member/logout")// 로그아웃
                .excludePathPatterns("/jwt/reissue")// 토큰 재발급
                .excludePathPatterns("/login/oauth2/code/kakao")// 카카오 로그인
                .excludePathPatterns("/auth/logout");// 카카오 로그아웃
    }

    @Autowired
    private LoginUserArgumentResolver loginUserArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(loginUserArgumentResolver);
    }

}

