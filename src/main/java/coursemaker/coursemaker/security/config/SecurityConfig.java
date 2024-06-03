package coursemaker.coursemaker.security.config;

import coursemaker.coursemaker.jwt.filter.JWTFilter;
import coursemaker.coursemaker.jwt.JWTUtil;
import coursemaker.coursemaker.oauth.handler.CustomSuccessHandler;
import coursemaker.coursemaker.oauth.service.CustomOAuth2UserService;
import coursemaker.coursemaker.security.handler.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;

//커스텀 순서 AF(authentication filter) -> AM(authentication manager)
//         ->AP(authentication provider) -> US(userdetails service)
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomSuccessHandler customSuccessHandler;
    private final JWTUtil jwtUtil;
    private final AuthenticationProvider authenticationProvider;
    private final AuthenticationProvider restAuthenticationProvider;
    private final AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> authenticationDetailsSource;
    private final FormAuthenticationSuccessHandler successHandler;
    private final FormAuthenticationFailureHandler failureHandler;
    private final RestAuthenticationSuccessHandler restSuccessHandler;
    private final RestAuthenticationFailureHandler restFailureHandler;


    //AuthenticationManager Bean 등록
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        /*모든 요청을 인증 없이 진행(개발 테스트용)*/
//        http
//                .authorizeHttpRequests((auth) -> auth
//                        .anyRequest().permitAll());

        http
                .cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {

                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {

                        CorsConfiguration configuration = new CorsConfiguration();
                        // 모든 출처에서 요청 허용 (http://localhost:3000와 같이 주소로 허용가능)
                        configuration.setAllowedOrigins(Collections.singletonList("*")); // http://localhost:3000와 같이 주소로 허용가능
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
                    }
                }));



        //csrf disable
        http
                .csrf((auth) -> auth.disable());


        //Form 로그인 방식 disable
        http
                .formLogin((auth) -> auth.disable());

        //HTTP Basic 인증 방식 disable
        http
                .httpBasic((auth) -> auth.disable());


        //JWTFilter 추가
        http
                .addFilterBefore(new JWTFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

        //oauth2소셜 카카오 로그인
        http
                .oauth2Login((oauth2) -> oauth2
                        .userInfoEndpoint((userInfoEndpointConfig) -> userInfoEndpointConfig
                                .userService(customOAuth2UserService))
                        .successHandler(customSuccessHandler)
                );

        //경로별 인가 작업
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/css/**", "/images/**", "/js/**", "/favicon/**").permitAll() //정적 자원 설정
                        .requestMatchers("/","/signup").permitAll()
                        .requestMatchers("/user").hasAuthority("ROLE_USER")
                        .requestMatchers("/traveler").hasAuthority("ROLE_TRAVELER")
                        .requestMatchers("/admin").hasAuthority("ROLE_ADMIN")
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .authenticationDetailsSource(authenticationDetailsSource)
                        .successHandler(successHandler)
                        .failureHandler(failureHandler)
                        .loginPage("/login")
                        .permitAll()//커스텀 로그인 페이지 설정
                )
                .authenticationProvider(authenticationProvider)
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler(new FormAccessDeniedHandler("/denied"))
                )
        ;
        //세션 설정 : STATELESS
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}