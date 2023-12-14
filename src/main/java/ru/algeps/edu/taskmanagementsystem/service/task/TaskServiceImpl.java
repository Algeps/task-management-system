package ru.algeps.edu.taskmanagementsystem.service.task;

import static ru.algeps.edu.taskmanagementsystem.mapper.TaskMapper.*;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotNull;
import java.util.Collections;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.algeps.edu.taskmanagementsystem.dto.task.TaskDto;
import ru.algeps.edu.taskmanagementsystem.dto.task.TaskEditOrCreateDto;
import ru.algeps.edu.taskmanagementsystem.dto.task.TaskStatusDto;
import ru.algeps.edu.taskmanagementsystem.exceptions.TaskServiceException;
import ru.algeps.edu.taskmanagementsystem.model.Comment;
import ru.algeps.edu.taskmanagementsystem.model.Task;
import ru.algeps.edu.taskmanagementsystem.model.User;
import ru.algeps.edu.taskmanagementsystem.repository.CommentRepository;
import ru.algeps.edu.taskmanagementsystem.repository.TaskRepository;
import ru.algeps.edu.taskmanagementsystem.repository.UserRepository;

@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {
  public static final int NUMBER_OF_RECENT_COMMENTS_WHEN_DISPLAYING_TASK = 5;
  private UserRepository userRepository;
  private TaskRepository taskRepository;
  private CommentRepository commentRepository;

  @Override
  public TaskDto create(@NonNull Long userAuthorId, @NonNull TaskEditOrCreateDto dto) {
    User userExecutor = getUserExecutor(dto);
    Task task = mapperToTask(dto, userRepository.getReferenceById(userAuthorId), userExecutor);
    return mapperToTaskDto(taskRepository.saveAndFlush(task), Collections.emptyList(), 0);
  }

  @Override
  public TaskDto read(@NonNull Long taskId) {
    Task task = getTaskById(taskId);
    Page<Comment> commentPage = getCommentPageForTask(task);
    return mapperToTaskDto(task, commentPage.getContent(), commentPage.getTotalElements());
  }

  @Override
  public TaskDto updateAsAuthor(
      @NotNull Long userAuthorId, @NonNull Long taskId, @NonNull TaskEditOrCreateDto dto) {
    Task task = getTaskById(taskId);
    if (!task.getUserAuthor().getUserId().equals(userAuthorId)) {
      throw new TaskServiceException("Only the author can change the task fields!");
    }

    User userExecutor = getUserExecutor(dto);
    task = taskRepository.saveAndFlush(updateTask(task, dto, userExecutor));
    Page<Comment> commentPage = getCommentPageForTask(task);
    return mapperToTaskDto(task, commentPage.getContent(), commentPage.getTotalElements());
  }

  /** Возвращает прокси-объект из БД, иначе - null */
  private User getUserExecutor(TaskEditOrCreateDto dto) {
    return dto.getUserExecutorId() == null
        ? null
        : userRepository.getReferenceById(dto.getUserExecutorId());
  }

  @Override
  public TaskDto updateAsExecutor(
      @NotNull Long userExecutorId, @NonNull Long taskId, @NonNull TaskStatusDto dto) {
    Task task = getTaskById(taskId);
    if (!task.getUserExecutor().getUserId().equals(userExecutorId)) {
      throw new TaskServiceException("Only the author can change the task fields!");
    }

    task = taskRepository.saveAndFlush(task.setStatus(dto.getStatus()));
    Page<Comment> commentPage = getCommentPageForTask(task);
    return mapperToTaskDto(task, commentPage.getContent(), commentPage.getTotalElements());
  }

  private Task getTaskById(Long taskId) {
    return taskRepository
        .findById(taskId)
        .orElseThrow(() -> new EntityNotFoundException("Task not found with id:" + taskId));
  }

  private Page<Comment> getCommentPageForTask(Task task) {
    return commentRepository.getCommentsDescOrder(
        task.getTaskId(), PageRequest.ofSize(NUMBER_OF_RECENT_COMMENTS_WHEN_DISPLAYING_TASK));
  }

  @Override
  public void delete(@NonNull Long userId, @NonNull Long taskId) {
    Long userAuthorId = taskRepository.getReferenceById(userId).getUserAuthor().getUserId();
    if (!userAuthorId.equals(taskId)) {
      throw new TaskServiceException("Only the author can delete the task!");
    }

    taskRepository.deleteById(taskId);
  }
}
