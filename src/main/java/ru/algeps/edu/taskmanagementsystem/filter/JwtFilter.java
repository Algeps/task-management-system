package ru.algeps.edu.taskmanagementsystem.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.algeps.edu.taskmanagementsystem.service.auth.jwt.JwtAuthService;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
  private static final String AUTHORIZATION_HEADER = "Authorization";
  private final JwtAuthService jwtAuthService;

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain)
      throws ServletException, IOException {
    String token = getAccessTokenFromRequest(request);
    if (token != null && jwtAuthService.validateAccessToken(token)) {
      SecurityContextHolder.getContext()
          .setAuthentication(jwtAuthService.getAuthenticationFromAccessToken(token));
    }
    filterChain.doFilter(request, response);
  }

  private String getAccessTokenFromRequest(HttpServletRequest request) {
    String bearer = request.getHeader(AUTHORIZATION_HEADER);
    if (StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")) {
      return bearer.substring(7);
    }
    return null;
  }
}
