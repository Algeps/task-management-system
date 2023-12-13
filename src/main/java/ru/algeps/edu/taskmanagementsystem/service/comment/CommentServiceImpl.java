package ru.algeps.edu.taskmanagementsystem.service.comment;

import static ru.algeps.edu.taskmanagementsystem.mapper.CommentMapper.*;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.algeps.edu.taskmanagementsystem.dto.CommentDto;
import ru.algeps.edu.taskmanagementsystem.dto.PaginationListDto;
import ru.algeps.edu.taskmanagementsystem.dto.PaginationParameterDto;
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
  public CommentDto create(CommentDto dto) {
    Task task = taskRepository.getReferenceById(dto.getTaskId());
    User userAuthor = userRepository.getReferenceById(dto.getUserAuthorId());
    return mapperToCommentDto(
        commentRepository.saveAndFlush(mapperToComment(dto, task, userAuthor)));
  }

  @Override
  public PaginationListDto<CommentDto> readAllPagination(
      Long taskId, PaginationParameterDto parameter) {
    Page<Comment> page =
        commentRepository.getCommentsDescOrder(
            taskId, PageRequest.of(parameter.getOffset(), parameter.getLimit()));

    return new PaginationListDto<>(
        mapperToListCommentDto(page.getContent()), page.getTotalElements());
  }
}
