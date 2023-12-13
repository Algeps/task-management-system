package ru.algeps.edu.taskmanagementsystem.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.algeps.edu.taskmanagementsystem.dto.user.UserDto;
import ru.algeps.edu.taskmanagementsystem.service.user.UserService;

@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
public class UserController {
  private UserService userService;

  @PostMapping("/")
  ResponseEntity<UserDto> create(@Valid @RequestBody UserDto dto) {
    return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(dto));
  }

  @GetMapping("/{id}")
  ResponseEntity<UserDto> read(@PathVariable @Valid @Positive Long id) {
    return ResponseEntity.ok(userService.read(id));
  }

  @PutMapping("/{id}")
  ResponseEntity<UserDto> update(
      @PathVariable @Valid @Positive Long id, @Valid @RequestBody UserDto dto) {
    return ResponseEntity.ok(userService.update(id, dto));
  }

  @DeleteMapping("/delete/{id}")
  ResponseEntity<Void> delete(@PathVariable @Valid @Positive Long id) {
    userService.delete(id);
    return ResponseEntity.ok().build();
  }
}
