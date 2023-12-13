package ru.algeps.edu.taskmanagementsystem.service.task;

import static ru.algeps.edu.taskmanagementsystem.mapper.TaskMapper.*;

import jakarta.persistence.EntityNotFoundException;
import java.util.Collections;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.algeps.edu.taskmanagementsystem.dto.TaskDto;
import ru.algeps.edu.taskmanagementsystem.dto.TaskEditOrCreateDto;
import ru.algeps.edu.taskmanagementsystem.model.Comment;
import ru.algeps.edu.taskmanagementsystem.model.Task;
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
  public TaskDto create(TaskEditOrCreateDto dto) {
    Task task =
        mapperToTask(
            dto,
            userRepository.getReferenceById(dto.getUserAuthorId()),
            dto.getUserExecutorId() == null
                ? null
                : userRepository.getReferenceById(dto.getUserExecutorId()));
    return mapperToTaskDto(taskRepository.saveAndFlush(task), Collections.emptyList(), 0);
  }

  @Override
  public TaskDto read(Long id) {
    Task task =
        taskRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Task not found with id:" + id));
    Page<Comment> commentPage =
        commentRepository.getCommentsDescOrder(
            task.getTaskId(), PageRequest.ofSize(NUMBER_OF_RECENT_COMMENTS_WHEN_DISPLAYING_TASK));
    return mapperToTaskDto(task, commentPage.getContent(), commentPage.getTotalElements());
  }

  @Override
  public TaskDto update(Long id, TaskEditOrCreateDto dto) {
    Task task =
        taskRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Task not found with id:" + id));
    task =
        taskRepository.saveAndFlush(
            updateTask(
                task,
                dto,
                dto.getUserExecutorId() == null
                    ? null
                    : userRepository.getReferenceById(dto.getUserExecutorId())));
    Page<Comment> commentPage =
        commentRepository.getCommentsDescOrder(
            task.getTaskId(), PageRequest.ofSize(NUMBER_OF_RECENT_COMMENTS_WHEN_DISPLAYING_TASK));
    return mapperToTaskDto(task, commentPage.getContent(), commentPage.getTotalElements());
  }

  @Override
  public void delete(Long id) {
    taskRepository.deleteById(id);
  }
}
