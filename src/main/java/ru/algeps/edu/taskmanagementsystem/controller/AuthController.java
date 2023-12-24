package ru.algeps.edu.taskmanagementsystem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.algeps.edu.taskmanagementsystem.dto.jwt.JwtRequest;
import ru.algeps.edu.taskmanagementsystem.dto.jwt.JwtResponse;
import ru.algeps.edu.taskmanagementsystem.dto.jwt.RefreshJwtRequest;
import ru.algeps.edu.taskmanagementsystem.dto.user.UserDto;
import ru.algeps.edu.taskmanagementsystem.service.auth.jwt.JwtAuthService;
import ru.algeps.edu.taskmanagementsystem.service.user.UserService;

@Tag(name = "Auth Controller", description = "Регистрация и аутентификация пользователя.")
@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {
  private UserService userService;
  private final JwtAuthService authService;

  @Operation(summary = "Аутентификация пользователя")
  @ApiResponses(@ApiResponse(responseCode = "200", description = "Успешная аутентификация"))
  @PostMapping("/login")
  public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest authRequest) {
    return ResponseEntity.ok(authService.login(authRequest));
  }

  @Operation(summary = "Регистрация пользователя")
  @ApiResponses(@ApiResponse(responseCode = "201", description = "Успешная регистрация"))
  @PostMapping("/reg")
  public ResponseEntity<UserDto> registration(@Valid @RequestBody UserDto dto) {
    return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(dto));
  }

  @Operation(summary = "Logout пользователя")
  @ApiResponses(
      @ApiResponse(
          responseCode = "200",
          description = "Успешный выход. Удаляет Refresh-токен из хранилища"))
  @PostMapping("/logout")
  public ResponseEntity<Void> logout(@RequestBody RefreshJwtRequest request) {
    authService.logout(request.refreshToken());
    return ResponseEntity.ok().build();
  }

  @Operation(summary = "Получение нового Access-токена")
  @PostMapping("token")
  public ResponseEntity<JwtResponse> getNewAccessToken(@RequestBody RefreshJwtRequest request) {
    return ResponseEntity.ok(authService.getAccessToken(request.refreshToken()));
  }

  @Operation(summary = "Получение нового Refresh-токена")
  @PostMapping("refresh")
  public ResponseEntity<JwtResponse> getNewRefreshToken(@RequestBody RefreshJwtRequest request) {
    return ResponseEntity.ok(authService.refresh(request.refreshToken()));
  }
}
