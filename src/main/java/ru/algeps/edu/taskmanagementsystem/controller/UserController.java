package ru.algeps.edu.taskmanagementsystem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.algeps.edu.taskmanagementsystem.dto.user.UserDto;
import ru.algeps.edu.taskmanagementsystem.service.auth.jwt.JwtAuthService;
import ru.algeps.edu.taskmanagementsystem.service.user.UserService;

@Tag(name = "User Controller", description = "Действия с аккаунтом пользователя")
@SecurityRequirement(name = "jwt")
@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
public class UserController {
  private UserService userService;
  private JwtAuthService jwtAuthService;

  @Operation(summary = "Запрос информации о пользователе")
  @GetMapping("/{id}")
  public ResponseEntity<UserDto> read(@PathVariable @Valid @Positive Long id) {
    return ResponseEntity.ok(userService.read(id));
  }

  @Operation(summary = "Обновление информации пользователе")
  @PutMapping("/")
  public ResponseEntity<UserDto> update(@Valid @RequestBody UserDto dto) {
    Long id = jwtAuthService.getAuthInfo().getId();
    return ResponseEntity.ok(userService.update(id, dto));
  }

  @Operation(summary = "Удаление пользователя")
  @DeleteMapping("/")
  public ResponseEntity<Void> delete() {
    Long id = jwtAuthService.getAuthInfo().getId();
    userService.delete(id);
    return ResponseEntity.ok().build();
  }
}
