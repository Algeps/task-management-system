package ru.algeps.edu.taskmanagementsystem.mapper;

import java.util.List;
import org.springframework.lang.Nullable;
import ru.algeps.edu.taskmanagementsystem.dto.TaskDto;
import ru.algeps.edu.taskmanagementsystem.dto.TaskEditOrCreateDto;
import ru.algeps.edu.taskmanagementsystem.model.Comment;
import ru.algeps.edu.taskmanagementsystem.model.Task;
import ru.algeps.edu.taskmanagementsystem.model.User;

public class TaskMapper {
  private TaskMapper() {}

  public static Task mapperToTask(TaskEditOrCreateDto dto, User userAuthor, User userExecutor) {
    if (dto == null) {
      return null;
    }

    return Task.builder()
        .header(dto.getHeader())
        .description(dto.getDescription())
        .status(dto.getStatus())
        .priority(dto.getPriority())
        .userAuthor(userAuthor)
        .userExecutor(userExecutor)
        .build();
  }

  public static Task updateTask(Task task, TaskEditOrCreateDto dto, User userExecutor) {
    if (task == null) {
      return null;
    }

    if (dto.getHeader() != null) task.setHeader(dto.getHeader());
    if (dto.getDescription() != null) task.setDescription(dto.getDescription());
    if (dto.getStatus() != null) task.setStatus(dto.getStatus());
    if (dto.getPriority() != null) task.setPriority(dto.getPriority());
    return task.setUserExecutor(userExecutor);
  }

  public static TaskDto mapperToTaskDto(
      Task task, @Nullable List<Comment> comments, long totalComments) {
    if (task == null) {
      return null;
    }

    return TaskDto.builder()
        .taskId(task.getTaskId())
        .header(task.getHeader())
        .description(task.getDescription())
        .status(task.getStatus())
        .priority(task.getPriority())
        .userAuthor(UserMapper.mapperToUserShortInfoDto(task.getUserAuthor()))
        .userExecutor(
            task.getUserExecutor() == null
                ? null
                : UserMapper.mapperToUserShortInfoDto(task.getUserExecutor()))
        .creationTimestamp(task.getCreationTimestamp())
        .fiveLastComments(CommentMapper.mapperToPaginationListCommentDto(comments, totalComments))
        .build();
  }
}
