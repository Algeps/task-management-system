package ru.algeps.edu.taskmanagementsystem.service.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import java.util.HashMap;
import java.util.Map;
import lombok.NonNull;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
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

  @Override
  public JwtResponse login(@NonNull JwtRequest authRequest) {
    User user = getUserFromRepository(authRequest.email());
    // todo здесь Bcrypt
    if (user.getPassword().equals(authRequest.password())) {
      String accessToken = jwtProvider.generateAccessToken(user);
      String refreshToken = jwtProvider.generateRefreshToken(user);
      refreshStorage.put(user.getEmail(), refreshToken);
      return new JwtResponse(accessToken, refreshToken);
    } else {
      throw new JwtAuthException("Incorrect login or password!");
    }
  }

  @Override
  public boolean validateAccessToken(@NonNull String token) {
    return isValidAccessToken(token);
  }

  @Override
  public JwtAuthentication getAuthenticationFromAccessToken(@NonNull String token) {
    Claims claims = jwtProvider.getAccessClaims(token);
    JwtAuthentication jwtInfoToken = JwtUtils.generate(claims);
    jwtInfoToken.setAuthenticated(true);
    return jwtInfoToken;
  }

  @Override
  public JwtResponse getAccessToken(@NonNull String refreshToken) {
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
  public JwtResponse refresh(@NonNull String refreshToken) {
    if (isValidRefreshToken(refreshToken)) {
      Claims claims = jwtProvider.getRefreshClaims(refreshToken);
      String login = claims.getSubject();
      String saveRefreshToken = refreshStorage.get(login);
      if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
        User user = getUserFromRepository(login);
        String accessToken = jwtProvider.generateAccessToken(user);
        String newRefreshToken = jwtProvider.generateRefreshToken(user);
        refreshStorage.put(user.getEmail(), newRefreshToken);
        return new JwtResponse(accessToken, newRefreshToken);
      }
    }
    throw new JwtAuthException("Invalid JWT token!");
  }

  private boolean isValidAccessToken(String token) {
    try {
      jwtProvider.validateAccessToken(token);
      return true;
    } catch (SignatureException se) {
      throw new JwtAuthException("Invalid signature", se);
    } catch (ExpiredJwtException eje) {
      throw new JwtAuthException("Token expired", eje);
    } catch (UnsupportedJwtException uje) {
      throw new JwtAuthException("Unsupported jwt", uje);
    } catch (MalformedJwtException mje) {
      throw new JwtAuthException("Malformed jwt", mje);
    } catch (RuntimeException re) {
      throw new JwtAuthException("invalid token", re);
    }
  }

  private boolean isValidRefreshToken(String token) throws JwtAuthException {
    try {
      jwtProvider.validateRefreshToken(token);
      return true;
    } catch (SignatureException se) {
      throw new JwtAuthException("Invalid signature", se);
    } catch (ExpiredJwtException eje) {
      throw new JwtAuthException("Token expired", eje);
    } catch (UnsupportedJwtException uje) {
      throw new JwtAuthException("Unsupported jwt", uje);
    } catch (MalformedJwtException mje) {
      throw new JwtAuthException("Malformed jwt", mje);
    } catch (RuntimeException re) {
      throw new JwtAuthException("invalid token", re);
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
