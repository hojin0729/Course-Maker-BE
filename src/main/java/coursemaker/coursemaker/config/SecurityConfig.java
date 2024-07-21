package coursemaker.coursemaker.config;


import coursemaker.coursemaker.domain.auth.filter.*;
import coursemaker.coursemaker.domain.auth.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Collections;

//커스텀 순서 AF(authentication filter) -> AM(authentication manager)
//         ->AP(authentication provider) -> US(userdetails service)
@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtProvider jwtProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        /*URL 접근 권한 설정*/
        http
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        .anyRequest().permitAll()
                );

        /*세션 무상태 설정*/
        http
                .setSharedObject(SessionManagementConfigurer.class,
                new SessionManagementConfigurer<HttpSecurity>()
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        /* csrf, http basic, 폼 로그인 비활성화 */
        http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable);

        //Cors
        http.cors(AbstractHttpConfigurer::disable);// disable 해야 cors 안터짐
        http.cors(corsCustomizer -> corsCustomizer.configurationSource(request -> {

            CorsConfiguration configuration = new CorsConfiguration();
            // 모든 출처에서 요청 허용 (http://localhost:3000와 같이 주소로 허용가능)
            configuration.setAllowedOrigins(Collections.singletonList("http://localhost:5173")); // http://localhost:3000와 같이 주소로 허용가능
            // HTTP 메소드(GET, POST 등 모든요청)의 요청을 허용합니다.
            configuration.setAllowedMethods(Collections.singletonList("*"));
            // 인증 정보(쿠키, 인증 토큰 등)의 전송을 허용합니다.
            configuration.setAllowCredentials(true);
            // 모든 HTTP 헤더의 요청을 허용합니다.
            configuration.setAllowedHeaders(Collections.singletonList("*"));
            // 최대 유효시간 설정
            configuration.setMaxAge(3600L);

            // 브라우저가 접근할 수 있도록 특정 응답 헤더를 노출합니다. 여기서는 "Set-Cookie"와 "Authorization"
            configuration.setExposedHeaders(Collections.singletonList("Set-Cookie"));
            configuration.setExposedHeaders(Collections.singletonList("Authorization"));

            return configuration;
        }));

        /*로그인 필터 등록*/
        http
                .addFilterAt(
                        emailLoginFilter(),
                        UsernamePasswordAuthenticationFilter.class
                )
                .addFilterBefore(new ExceptionHandlerFilter(), EmailLoginFilter.class);
        
        /*JWT 검증 필터 등록*/
        http
                .addFilterBefore(new JwtAuthenticationFilter(jwtProvider), EmailLoginFilter.class)
                .addFilterBefore(new ExceptionHandlerFilter(), JwtAuthenticationFilter.class);

        /*로그아웃 필터 설정*/
        http.addFilterBefore(new ExceptionHandlerFilter(), LogoutFilter.class);
        http
                .logout(config -> config
                        .logoutUrl("/v1/auth/logout")
                        .addLogoutHandler(new EmailLogoutHandler(jwtProvider))
                        .logoutSuccessHandler(((request, response, authentication) -> SecurityContextHolder.clearContext()))
                );

//        //oauth2
//        http
//                .oauth2Login(Customizer.withDefaults());

        return http.build();
    }

    /*이메일 로그인 필터*/
    private EmailLoginFilter emailLoginFilter() throws Exception {
        EmailLoginFilter emailLoginFilter = new EmailLoginFilter(authenticationManager(authenticationConfiguration), jwtProvider);
        emailLoginFilter.setFilterProcessesUrl("/v1/auth/login");
        return emailLoginFilter;
    }

    /*AuthenticationManager 등록*/
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }


    /*비밀번호 암호화*/
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }



}
