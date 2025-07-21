package com.example.ayen;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.SecurityFilterChain;
import com.example.ayen.service.UserService;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.context.annotation.Bean;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final UserService userService;

    // ✅ 생성자 통해 UserService를 DI
    public SecurityConfig(UserService userService) {
        this.userService = userService;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("https://chany.ai.kr"); // 프론트 주소
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/error", "/login", "/login/oauth2/**", "/logout").permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) -> {
                            if (request.getRequestURI().startsWith("/api/")) {
                                response.sendError(HttpStatus.UNAUTHORIZED.value(), "Unauthorized");
                            } else {
                                response.sendRedirect("/");
                            }
                        })
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/oauth2/authorization/kakao")
                        .userInfoEndpoint(userInfo -> userInfo.userService(userService))
                        .defaultSuccessUrl("https://chany.ai.kr/scenarios", true)
                )
                .formLogin(form -> form.disable())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );

        return http.build();
    }
}
