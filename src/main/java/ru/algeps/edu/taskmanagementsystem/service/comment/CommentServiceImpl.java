package ru.algeps.edu.taskmanagementsystem.service.comment;

import static ru.algeps.edu.taskmanagementsystem.mapper.CommentMapper.*;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.algeps.edu.taskmanagementsystem.dto.PaginationListDto;
import ru.algeps.edu.taskmanagementsystem.dto.PaginationParameterDto;
import ru.algeps.edu.taskmanagementsystem.dto.comment.CommentCreateDto;
import ru.algeps.edu.taskmanagementsystem.dto.comment.CommentDto;
import ru.algeps.edu.taskmanagementsystem.model.Comment;
import ru.algeps.edu.taskmanagementsystem.model.Task;
import ru.algeps.edu.taskmanagementsystem.model.User;
import ru.algeps.edu.taskmanagementsystem.repository.CommentRepository;
import ru.algeps.edu.taskmanagementsystem.repository.TaskRepository;
import ru.algeps.edu.taskmanagementsystem.repository.UserRepository;

@AllArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {
  private CommentRepository commentRepository;
  private UserRepository userRepository;
  private TaskRepository taskRepository;

  @Override
  public CommentDto create(@NotNull CommentCreateDto dto, @NotNull Long userAuthorId) {
    Task task = taskRepository.getReferenceById(dto.taskId());
    User userAuthor = userRepository.getReferenceById(userAuthorId);
    return mapperToCommentDto(
        commentRepository.saveAndFlush(mapperToComment(dto, task, userAuthor)));
  }

  @Override
  public PaginationListDto<CommentDto> readAllPagination(
      @NotNull Long taskId, @NotNull PaginationParameterDto parameter) {
    if (!taskRepository.existsById(taskId)) {
      throw new EntityNotFoundException("Task not found with id:" + taskId);
    }
    Page<Comment> page =
        commentRepository.getCommentsDescOrder(
            taskId, PageRequest.of(parameter.getOffset(), parameter.getLimit()));

    return mapperToPaginationListCommentDto(page.getContent(), page.getTotalElements());
  }
}
