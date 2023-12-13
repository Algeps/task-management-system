package ru.algeps.edu.taskmanagementsystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration {
  @Bean
  protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(request -> request.anyRequest().permitAll());
    http.csrf(AbstractHttpConfigurer::disable);
    return http.build();
  }
}
