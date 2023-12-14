package ru.algeps.edu.taskmanagementsystem.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
import ru.algeps.edu.taskmanagementsystem.service.auth.jwt.JwtAuthService;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {
  private static final String AUTHORIZATION_HEADER = "Authorization";
  private final JwtAuthService jwtAuthService;

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
      throws IOException, ServletException {
    String token = getAccessTokenFromRequest((HttpServletRequest) request);
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
