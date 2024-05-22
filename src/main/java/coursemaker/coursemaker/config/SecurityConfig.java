package coursemaker.coursemaker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        
        /*개발용(CSRF disable)*/
        http
                .csrf((csrf) -> csrf.disable());
        
        /*모든 요청을 인증 없이 진행(개발 테스트용)*/
        http
                .authorizeHttpRequests((auth) -> auth
                        .anyRequest().permitAll());
        
        return http.build();
    }
}
