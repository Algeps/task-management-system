package ru.algeps.edu.taskmanagementsystem.service.task;

import static ru.algeps.edu.taskmanagementsystem.mapper.TaskMapper.*;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotNull;
import java.util.Collections;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.algeps.edu.taskmanagementsystem.dto.PaginationListDto;
import ru.algeps.edu.taskmanagementsystem.dto.PaginationParameterDto;
import ru.algeps.edu.taskmanagementsystem.dto.task.TaskDto;
import ru.algeps.edu.taskmanagementsystem.dto.task.TaskEditOrCreateDto;
import ru.algeps.edu.taskmanagementsystem.dto.task.TaskStatusDto;
import ru.algeps.edu.taskmanagementsystem.enums.TaskSort;
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
  public TaskDto create(@NotNull Long userAuthorId, @NotNull TaskEditOrCreateDto dto) {
    User userExecutor = getUserExecutor(dto);
    Task task = mapperToTask(dto, userRepository.getReferenceById(userAuthorId), userExecutor);
    Task newTask = getTaskById(taskRepository.saveAndFlush(task).getTaskId());
    return mapperToTaskDto(newTask, Collections.emptyList(), 0);
  }

  @Override
  public TaskDto read(@NotNull Long taskId) {
    Task task = getTaskById(taskId);
    Page<Comment> commentPage = getCommentPageForTask(task);
    return mapperToTaskDto(task, commentPage.getContent(), commentPage.getTotalElements());
  }

  @Override
  public PaginationListDto<TaskDto> readAllPagination(
      @NotNull Long userId, @NotNull PaginationParameterDto parameter, @NotNull TaskSort taskSort) {
    Page<Task> pageTask =
        taskRepository.getPaginationTaskWithUsers(
            userId,
            PageRequest.of(
                parameter.getOffset(), parameter.getLimit(), Sort.by(taskSort.getTitle())));

    return mapperToPaginationTaskDto(pageTask.getContent(), pageTask.getTotalElements());
  }

  @Override
  public TaskDto updateAsAuthor(
      @NotNull Long userAuthorId, @NotNull Long taskId, @NotNull TaskEditOrCreateDto dto) {
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
      @NotNull Long userExecutorId, @NotNull Long taskId, @NotNull TaskStatusDto dto) {
    Task task = getTaskById(taskId);
    if (task.getUserExecutor() == null
        || !task.getUserExecutor().getUserId().equals(userExecutorId)) {
      throw new TaskServiceException("Only the author can change the task fields!");
    }

    task = taskRepository.saveAndFlush(task.setStatus(dto.getStatus()));
    Page<Comment> commentPage = getCommentPageForTask(task);
    return mapperToTaskDto(task, commentPage.getContent(), commentPage.getTotalElements());
  }

  private Page<Comment> getCommentPageForTask(Task task) {
    return commentRepository.getCommentsDescOrder(
        task.getTaskId(), PageRequest.ofSize(NUMBER_OF_RECENT_COMMENTS_WHEN_DISPLAYING_TASK));
  }

  @Override
  public void delete(@NotNull Long userId, @NotNull Long taskId) {
    Long userAuthorId = getTaskById(taskId).getUserAuthor().getUserId();
    if (!userAuthorId.equals(userId)) {
      throw new TaskServiceException("Only the author can delete the task!");
    }

    taskRepository.deleteById(taskId);
  }

  private Task getTaskById(Long taskId) {
    return taskRepository
        .findById(taskId)
        .orElseThrow(() -> new EntityNotFoundException("Task not found with id:" + taskId));
  }
}
