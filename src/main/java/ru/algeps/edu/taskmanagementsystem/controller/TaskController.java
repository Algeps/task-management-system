package ru.algeps.edu.taskmanagementsystem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.algeps.edu.taskmanagementsystem.dto.PaginationListDto;
import ru.algeps.edu.taskmanagementsystem.dto.PaginationParameterDto;
import ru.algeps.edu.taskmanagementsystem.dto.task.TaskDto;
import ru.algeps.edu.taskmanagementsystem.dto.task.TaskEditOrCreateDto;
import ru.algeps.edu.taskmanagementsystem.dto.task.TaskStatusDto;
import ru.algeps.edu.taskmanagementsystem.enums.TaskSort;
import ru.algeps.edu.taskmanagementsystem.service.auth.jwt.JwtAuthService;
import ru.algeps.edu.taskmanagementsystem.service.task.TaskService;

@Tag(name = "Task Controller", description = "CRUD task")
@SecurityRequirement(name = "jwt")
@RestController
@RequestMapping("/api/task")
@AllArgsConstructor
public class TaskController {
  private TaskService taskService;
  private JwtAuthService jwtAuthService;

  @Operation(summary = "Создание задачи")
  @PostMapping("/")
  public ResponseEntity<TaskDto> create(@RequestBody TaskEditOrCreateDto task) {
    Long userId = jwtAuthService.getAuthInfo().getId();
    return ResponseEntity.status(HttpStatus.CREATED).body(taskService.create(userId, task));
  }

  @Operation(summary = "Запрос задачи по id")
  @GetMapping("/{taskId}")
  public ResponseEntity<TaskDto> read(@PathVariable @Valid @Positive Long taskId) {
    return ResponseEntity.ok(taskService.read(taskId));
  }

  @Operation(
      summary =
          "Запрос задач по id пользователя. Задачи как в роли автора, так и в роди исполнителя")
  @GetMapping("/list/{userId}")
  public ResponseEntity<PaginationListDto<TaskDto>> readUserTasksAsExecutor(
      @PathVariable @Valid @Positive Long userId,
      @Valid PaginationParameterDto dto,
      TaskSort taskSort) {
    return ResponseEntity.ok(taskService.readAllPagination(userId, dto, taskSort));
  }

  @Operation(summary = "Изменение данных задачи Автором")
  @PutMapping("/author/{taskId}")
  public ResponseEntity<TaskDto> updateAsAuthor(
      @PathVariable @Valid @Positive Long taskId, @RequestBody TaskEditOrCreateDto dto) {
    Long userId = jwtAuthService.getAuthInfo().getId();
    return ResponseEntity.ok(taskService.updateAsAuthor(userId, taskId, dto));
  }

  @Operation(summary = "Изменение статуса задачи Исполнителем")
  @PutMapping("/executor/{taskId}")
  public ResponseEntity<TaskDto> updateAsExecutor(
      @PathVariable @Valid @Positive Long taskId, @RequestBody TaskStatusDto dto) {
    Long userId = jwtAuthService.getAuthInfo().getId();
    return ResponseEntity.ok(taskService.updateAsExecutor(userId, taskId, dto));
  }

  @Operation(summary = "Удаление задачи")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable @Valid @Positive Long id) {
    Long userId = jwtAuthService.getAuthInfo().getId();
    taskService.delete(userId, id);
    return ResponseEntity.ok().build();
  }
}
