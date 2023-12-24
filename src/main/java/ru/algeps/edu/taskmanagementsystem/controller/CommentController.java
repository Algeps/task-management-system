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
import ru.algeps.edu.taskmanagementsystem.dto.comment.CommentCreateDto;
import ru.algeps.edu.taskmanagementsystem.dto.comment.CommentDto;
import ru.algeps.edu.taskmanagementsystem.service.auth.jwt.JwtAuthService;
import ru.algeps.edu.taskmanagementsystem.service.comment.CommentService;

@Tag(name = "Comment Controller", description = "API комментариев. Создание и постраничное чтение.")
@SecurityRequirement(name = "jwt")
@RestController
@RequestMapping("/api/comment")
@AllArgsConstructor
public class CommentController {
  private CommentService commentService;
  private JwtAuthService jwtAuthService;

  @Operation(summary = "Создание комментария")
  @PostMapping("/")
  public ResponseEntity<CommentDto> create(@Valid @RequestBody CommentCreateDto dto) {
    Long userId = jwtAuthService.getAuthInfo().getId();
    return ResponseEntity.status(HttpStatus.CREATED).body(commentService.create(dto, userId));
  }

  @Operation(summary = "Постраничный список комментариев")
  @GetMapping("/{taskId}")
  public ResponseEntity<PaginationListDto<CommentDto>> read(
      @PathVariable @Valid @Positive Long taskId, @Valid PaginationParameterDto dto) {
    return ResponseEntity.ok(commentService.readAllPagination(taskId, dto));
  }
}
