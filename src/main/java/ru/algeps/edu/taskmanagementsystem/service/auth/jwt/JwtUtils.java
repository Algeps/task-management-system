package ru.algeps.edu.taskmanagementsystem.service.auth.jwt;

import io.jsonwebtoken.Claims;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.algeps.edu.taskmanagementsystem.model.JwtAuthentication;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtUtils {
  public static JwtAuthentication generate(Claims claims) {
    JwtAuthentication jwtInfoToken = new JwtAuthentication();
    jwtInfoToken.setId(claims.get("idUser", Long.class));
    jwtInfoToken.setUsername(claims.getSubject());
    return jwtInfoToken;
  }
}
