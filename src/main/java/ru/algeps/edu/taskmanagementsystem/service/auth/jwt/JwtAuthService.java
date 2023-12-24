package ru.algeps.edu.taskmanagementsystem.service.auth.jwt;

import ru.algeps.edu.taskmanagementsystem.dto.jwt.JwtRequest;
import ru.algeps.edu.taskmanagementsystem.dto.jwt.JwtResponse;
import ru.algeps.edu.taskmanagementsystem.exceptions.JwtAuthException;
import ru.algeps.edu.taskmanagementsystem.model.JwtAuthentication;

public interface JwtAuthService {
  JwtResponse login(JwtRequest authRequest) throws JwtAuthException;
  void logout(String token);

  boolean validateAccessToken(String token) throws JwtAuthException;

  JwtAuthentication getAuthenticationFromAccessToken(String token);

  JwtResponse getAccessToken(String refreshToken) throws JwtAuthException;

  JwtResponse refresh(String refreshToken) throws JwtAuthException;

  JwtAuthentication getAuthInfo();
}
