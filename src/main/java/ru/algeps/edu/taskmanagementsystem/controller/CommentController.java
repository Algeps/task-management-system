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
import ru.algeps.edu.taskmanagementsystem.dto.comment.CommentDto;
import ru.algeps.edu.taskmanagementsystem.service.comment.CommentService;

@Tag(name = "Comment Controller", description = "API комментариев. Создание и постраничное чтение.")
@SecurityRequirement(name = "jwt")
@RestController
@RequestMapping("/api/comment")
@AllArgsConstructor
public class CommentController {
  private CommentService commentService;

  @Operation(summary = "Создание комментария")
  @PostMapping("/")
  ResponseEntity<CommentDto> create(@Valid @RequestBody CommentDto dto) {
    return ResponseEntity.status(HttpStatus.CREATED).body(commentService.create(dto));
  }

  @Operation(summary = "Постраничный список комментариев")
  @GetMapping("/{taskId}")
  ResponseEntity<PaginationListDto<CommentDto>> read(
      @PathVariable @Valid @Positive Long taskId, @Valid PaginationParameterDto dto) {
    return ResponseEntity.ok(commentService.readAllPagination(taskId, dto));
  }
}
