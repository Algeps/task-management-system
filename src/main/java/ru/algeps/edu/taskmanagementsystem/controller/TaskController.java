package ru.algeps.edu.taskmanagementsystem.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.algeps.edu.taskmanagementsystem.dto.TaskDto;
import ru.algeps.edu.taskmanagementsystem.dto.TaskEditOrCreateDto;
import ru.algeps.edu.taskmanagementsystem.service.task.TaskService;

@RestController
@RequestMapping("/api/task")
@AllArgsConstructor
public class TaskController {
  private TaskService taskService;

  // todo 1) Пользователи могут просматривать задачи других пользователей, а исполнители задачи
  //   могут менять статус своих задач.
  //    2) Конкретного автора и исполнителя.

  @PostMapping("/")
  ResponseEntity<TaskDto> create(@RequestBody TaskEditOrCreateDto task) {
    return ResponseEntity.status(HttpStatus.CREATED).body(taskService.create(task));
  }

  @GetMapping("/{id}")
  ResponseEntity<TaskDto> read(@PathVariable @Valid @Positive Long id) {
    return ResponseEntity.ok(taskService.read(id));
  }

  @PutMapping("/{id}")
  ResponseEntity<TaskDto> update(
      @PathVariable @Valid @Positive Long id, @RequestBody TaskEditOrCreateDto task) {
    return ResponseEntity.ok(taskService.update(id, task));
  }

  @DeleteMapping("/{id}")
  ResponseEntity<Void> delete(@PathVariable @Valid @Positive Long id) {
    taskService.delete(id);
    return ResponseEntity.ok().build();
  }
}
