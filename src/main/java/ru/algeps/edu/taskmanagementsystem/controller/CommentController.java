package ru.algeps.edu.taskmanagementsystem.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.algeps.edu.taskmanagementsystem.dto.CommentDto;
import ru.algeps.edu.taskmanagementsystem.dto.PaginationListDto;
import ru.algeps.edu.taskmanagementsystem.dto.PaginationParameterDto;
import ru.algeps.edu.taskmanagementsystem.service.comment.CommentService;

@RestController
@RequestMapping("/api/comment")
@AllArgsConstructor
public class CommentController {
  private CommentService commentService;

  @PostMapping("/")
  ResponseEntity<CommentDto> create(@Valid @RequestBody CommentDto dto) {
    return ResponseEntity.status(HttpStatus.CREATED).body(commentService.create(dto));
  }

  @GetMapping("/{id}")
  ResponseEntity<PaginationListDto<CommentDto>> read(
      @PathVariable @Valid @Positive Long id, @RequestParam PaginationParameterDto dto) {
    return ResponseEntity.ok(commentService.readAllPagination(id, dto));
  }
}
