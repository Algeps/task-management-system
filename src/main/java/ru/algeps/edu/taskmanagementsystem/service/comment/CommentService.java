package ru.algeps.edu.taskmanagementsystem.service.comment;

import org.springframework.stereotype.Service;
import ru.algeps.edu.taskmanagementsystem.dto.comment.CommentDto;
import ru.algeps.edu.taskmanagementsystem.dto.PaginationListDto;
import ru.algeps.edu.taskmanagementsystem.dto.PaginationParameterDto;

@Service
public interface CommentService {
  CommentDto create(CommentDto dto);

  PaginationListDto<CommentDto> readAllPagination(Long taskId, PaginationParameterDto parameter);
}
