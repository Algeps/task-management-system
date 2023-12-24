package ru.algeps.edu.taskmanagementsystem.service.auth.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.algeps.edu.taskmanagementsystem.model.User;

@Slf4j
@Component
public class JwtProvider {
  private final Long lifeTimeSecondsAccessToken;
  private final Long lifeTimeSecondsRefreshToken;
  private final SecretKey jwtAccessSecret;
  private final SecretKey jwtRefreshSecret;

  public JwtProvider(
      @Value("${jwt.secret.access}") String jwtAccessSecret,
      @Value("${jwt.secret.refresh}") String jwtRefreshSecret,
      @Value("${jwt.lifetime.seconds.access}") Long lifeTimeSecondsAccessToken,
      @Value("${jwt.lifetime.seconds.refresh}") Long lifeTimeSecondsRefreshToken) {
    this.jwtAccessSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtAccessSecret));
    this.jwtRefreshSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtRefreshSecret));
    this.lifeTimeSecondsAccessToken = lifeTimeSecondsAccessToken;
    this.lifeTimeSecondsRefreshToken = lifeTimeSecondsRefreshToken;
  }

  /** Access-токена - токен доступа. Каждые 5 минут выдаётся новый. */
  public String generateAccessToken(@NotNull User user) {
    LocalDateTime now = LocalDateTime.now();
    Instant accessExpirationInstant =
        now.plusSeconds(lifeTimeSecondsAccessToken).atZone(ZoneId.systemDefault()).toInstant();
    Date accessExpiration = Date.from(accessExpirationInstant);
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
  public String generateRefreshToken(@NotNull User user) {
    LocalDateTime now = LocalDateTime.now();
    Instant refreshExpirationInstant =
        now.plusSeconds(lifeTimeSecondsRefreshToken).atZone(ZoneId.systemDefault()).toInstant();
    Date refreshExpiration = Date.from(refreshExpirationInstant);
    return Jwts.builder()
        .subject(user.getEmail())
        .expiration(refreshExpiration)
        .signWith(jwtRefreshSecret)
        .compact();
  }

  public boolean validateAccessToken(@NotNull String accessToken) throws JwtException {
    return validateToken(accessToken, jwtAccessSecret);
  }

  public boolean validateRefreshToken(@NotNull String refreshToken) throws JwtException {
    return validateToken(refreshToken, jwtRefreshSecret);
  }

  private boolean validateToken(@NotNull String token, @NotNull SecretKey secret)
      throws JwtException {
    Jwts.parser().verifyWith(secret).build().parseSignedClaims(token);
    return true;
  }

  public Claims getAccessClaims(@NotNull String token) {
    return getClaims(token, jwtAccessSecret);
  }

  public Claims getRefreshClaims(@NotNull String token) {
    return getClaims(token, jwtRefreshSecret);
  }

  private Claims getClaims(@NotNull String token, @NotNull SecretKey secret) {
    return Jwts.parser().verifyWith(secret).build().parseSignedClaims(token).getPayload();
  }
}
