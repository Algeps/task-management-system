package ru.algeps.edu.taskmanagementsystem.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import ru.algeps.edu.taskmanagementsystem.filter.JwtFilter;
import ru.algeps.edu.taskmanagementsystem.handler.FilterChainExceptionHandler;

@SecurityScheme(type = SecuritySchemeType.HTTP, name = "jwt", scheme = "bearer")
@EnableWebSecurity
@Configuration
@AllArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {
  private final FilterChainExceptionHandler filterChainExceptionHandler;
  private final JwtFilter jwtFilter;
  private static final String[] AUTH_WHITELIST = {
    "/api-docs",
    "/v2/api-docs",
    "/swagger-resources",
    "/swagger-resources/**",
    "/configuration/ui",
    "/configuration/security",
    "/swagger-ui.html",
    "/v3/api-docs/**",
    "/swagger-ui/**"
  };

  @Bean
  protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http.httpBasic(AbstractHttpConfigurer::disable)
        .csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(
            sessionManagement ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(
            request ->
                request
                    .requestMatchers("/api/auth/**")
                    .permitAll()
                    .requestMatchers(AUTH_WHITELIST)
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .addFilterAfter(filterChainExceptionHandler, LogoutFilter.class)
        .addFilterAfter(jwtFilter, UsernamePasswordAuthenticationFilter.class)
        .build();
  }
}
