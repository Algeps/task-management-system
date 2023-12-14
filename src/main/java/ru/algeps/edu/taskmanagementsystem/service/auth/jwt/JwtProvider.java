package ru.algeps.edu.taskmanagementsystem.service.auth.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.algeps.edu.taskmanagementsystem.model.User;

@Slf4j
@Component
public class JwtProvider {
  // todo время жизни метки тоже нужно настраивать из application.properties
  private final SecretKey jwtAccessSecret;
  private final SecretKey jwtRefreshSecret;

  public JwtProvider(
      @Value("${jwt.secret.access}") String jwtAccessSecret,
      @Value("${jwt.secret.refresh}") String jwtRefreshSecret) {
    this.jwtAccessSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtAccessSecret));
    this.jwtRefreshSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtRefreshSecret));
  }

  /** Access-токена - токен доступа. Каждые 5 минут выдаётся новый. */
  public String generateAccessToken(@NonNull User user) {
    final LocalDateTime now = LocalDateTime.now();
    final Instant accessExpirationInstant =
        now.plusMinutes(5).atZone(ZoneId.systemDefault()).toInstant();
    final Date accessExpiration = Date.from(accessExpirationInstant);
    return Jwts.builder()
        .subject(user.getEmail())
        .expiration(accessExpiration)
        .signWith(jwtAccessSecret)
        .claim("idUser", user.getUserId())
        .compact();
  }

  /**
   * Refresh токен служит для обновления текущего Access-токена и Access-токена. Каждые 30 дней
   * выдаётся новый.
   */
  public String generateRefreshToken(@NonNull User user) {
    final LocalDateTime now = LocalDateTime.now();
    final Instant refreshExpirationInstant =
        now.plusDays(30).atZone(ZoneId.systemDefault()).toInstant();
    final Date refreshExpiration = Date.from(refreshExpirationInstant);
    return Jwts.builder()
        .subject(user.getEmail())
        .expiration(refreshExpiration)
        .signWith(jwtRefreshSecret)
        .compact();
  }

  public boolean validateAccessToken(@NonNull String accessToken) {
    return validateToken(accessToken, jwtAccessSecret);
  }

  public boolean validateRefreshToken(@NonNull String refreshToken) {
    return validateToken(refreshToken, jwtRefreshSecret);
  }

  private boolean validateToken(@NonNull String token, @NonNull SecretKey secret)
      throws JwtException {
    Jwts.parser().verifyWith(secret).build().parseSignedClaims(token);
    return true;
  }

  public Claims getAccessClaims(@NonNull String token) {
    return getClaims(token, jwtAccessSecret);
  }

  public Claims getRefreshClaims(@NonNull String token) {
    return getClaims(token, jwtRefreshSecret);
  }

  private Claims getClaims(@NonNull String token, @NonNull SecretKey secret) {
    return Jwts.parser().verifyWith(secret).build().parseSignedClaims(token).getPayload();
  }
}
