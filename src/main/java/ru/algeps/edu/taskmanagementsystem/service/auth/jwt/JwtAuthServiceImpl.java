package ru.algeps.edu.taskmanagementsystem.service.auth.jwt;

import io.jsonwebtoken.*;
import jakarta.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.algeps.edu.taskmanagementsystem.dto.jwt.JwtRequest;
import ru.algeps.edu.taskmanagementsystem.dto.jwt.JwtResponse;
import ru.algeps.edu.taskmanagementsystem.exceptions.JwtAuthException;
import ru.algeps.edu.taskmanagementsystem.model.JwtAuthentication;
import ru.algeps.edu.taskmanagementsystem.model.User;
import ru.algeps.edu.taskmanagementsystem.repository.UserRepository;

@Service
@AllArgsConstructor
public class JwtAuthServiceImpl implements JwtAuthService {
  private final UserRepository userRepository;
  private final Map<String, String> refreshStorage = new HashMap<>();
  private final JwtProvider jwtProvider;
  private final PasswordEncoder passwordEncoder;

  @Override
  public JwtResponse login(@NotNull JwtRequest authRequest) throws JwtAuthException {
    User user = getUserFromRepository(authRequest.email());
    if (isCorrectPassword(user.getPassword(), authRequest.password())) {
      String accessToken = jwtProvider.generateAccessToken(user);
      String refreshToken = jwtProvider.generateRefreshToken(user);
      refreshStorage.put(user.getEmail(), refreshToken);
      return new JwtResponse(accessToken, refreshToken);
    } else {
      throw new JwtAuthException("Incorrect login or password!");
    }
  }

  @Override
  public void logout(@NotNull String token) {
    refreshStorage.remove(token);
  }

  private boolean isCorrectPassword(String expectedPassword, String actualPassword) {
    return passwordEncoder.matches(actualPassword, expectedPassword);
  }

  @Override
  public boolean validateAccessToken(@NotNull String token) throws JwtAuthException {
    return isValidAccessToken(token);
  }

  @Override
  public JwtAuthentication getAuthenticationFromAccessToken(@NotNull String token) {
    Claims claims = jwtProvider.getAccessClaims(token);
    JwtAuthentication jwtInfoToken = JwtUtils.generate(claims);
    jwtInfoToken.setAuthenticated(true);
    return jwtInfoToken;
  }

  @Override
  public JwtResponse getAccessToken(@NotNull String refreshToken) throws JwtAuthException {
    if (isValidRefreshToken(refreshToken)) {
      Claims claims = jwtProvider.getRefreshClaims(refreshToken);
      String email = claims.getSubject();
      String saveRefreshToken = refreshStorage.get(email);
      if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
        User user = getUserFromRepository(email);
        String accessToken = jwtProvider.generateAccessToken(user);
        return new JwtResponse(accessToken, refreshToken);
      }
    }
    throw new JwtAuthException("Invalid JWT token!");
  }

  @Override
  public JwtResponse refresh(@NotNull String refreshToken) throws JwtAuthException {
    if (isValidRefreshToken(refreshToken)) {
      Claims claims = jwtProvider.getRefreshClaims(refreshToken);
      String email = claims.getSubject();
      String saveRefreshToken = refreshStorage.get(email);
      if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
        User user = getUserFromRepository(email);
        String accessToken = jwtProvider.generateAccessToken(user);
        String newRefreshToken = jwtProvider.generateRefreshToken(user);
        refreshStorage.put(user.getEmail(), newRefreshToken);
        return new JwtResponse(accessToken, newRefreshToken);
      }
    }
    throw new JwtAuthException("Invalid JWT token!");
  }

  private boolean isValidAccessToken(String token) throws JwtAuthException {
    try {
      return jwtProvider.validateAccessToken(token);
    } catch (JwtException e) {
      throw new JwtAuthException("Access token: " + e.getLocalizedMessage());
    } catch (RuntimeException re) {
      throw new JwtAuthException("Invalid Access token", re);
    }
  }

  private boolean isValidRefreshToken(String token) throws JwtAuthException {
    try {
      return jwtProvider.validateRefreshToken(token);
    } catch (JwtException e) {
      throw new JwtAuthException("Refresh token:" + e.getLocalizedMessage());
    } catch (RuntimeException re) {
      throw new JwtAuthException("Invalid Refresh token", re);
    }
  }

  private User getUserFromRepository(String email) {
    return userRepository
        .getByEmail(email)
        .orElseThrow(() -> new JwtAuthException("User not found!"));
  }

  @Override
  public JwtAuthentication getAuthInfo() {
    return (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
  }
}
