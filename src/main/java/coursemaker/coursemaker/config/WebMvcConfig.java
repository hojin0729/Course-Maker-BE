package coursemaker.coursemaker.config;

import coursemaker.coursemaker.jwt.JwtInterceptor;
import coursemaker.coursemaker.jwt.JwtTokenProvider;
import coursemaker.coursemaker.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

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

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(JwtInterceptor)
                .excludePathPatterns("/v1/member/**");// 멤버도메인은 예외로
    }

}

