package ru.algeps.edu.taskmanagementsystem.service.auth.jwt;

import ru.algeps.edu.taskmanagementsystem.dto.jwt.JwtRequest;
import ru.algeps.edu.taskmanagementsystem.dto.jwt.JwtResponse;
import ru.algeps.edu.taskmanagementsystem.model.JwtAuthentication;

public interface JwtAuthService {
  JwtResponse login(JwtRequest authRequest);
  boolean validateAccessToken(String token);
  JwtAuthentication getAuthenticationFromAccessToken(String token);

  JwtResponse getAccessToken(String refreshToken);

  JwtResponse refresh(String refreshToken);

  JwtAuthentication getAuthInfo();
}
