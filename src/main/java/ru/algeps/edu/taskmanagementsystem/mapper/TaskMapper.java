package ru.algeps.edu.taskmanagementsystem.mapper;

import java.util.Collections;
import java.util.List;
import org.springframework.lang.Nullable;
import ru.algeps.edu.taskmanagementsystem.dto.PaginationListDto;
import ru.algeps.edu.taskmanagementsystem.dto.task.TaskDto;
import ru.algeps.edu.taskmanagementsystem.dto.task.TaskEditOrCreateDto;
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
        .status(task.getStatus().getTitle())
        .priority(task.getPriority().getTitle())
        .userAuthor(UserMapper.mapperToUserShortInfoDto(task.getUserAuthor()))
        .userExecutor(
            task.getUserExecutor() == null
                ? null
                : UserMapper.mapperToUserShortInfoDto(task.getUserExecutor()))
        .creationTimestamp(task.getCreationTimestamp())
        .updateTimestamp(task.getUpdateTimestamp())
        .fiveLastComments(CommentMapper.mapperToPaginationListCommentDto(comments, totalComments))
        .build();
  }

  public static TaskDto mapperToTaskDtoWithoutComments(Task task) {
    if (task == null) {
      return null;
    }

    return mapperToTaskDto(task, null, 0);
  }

  public static List<TaskDto> mapperToListTaskDto(List<Task> list) {
    if (list == null) {
      return Collections.emptyList();
    }

    return list.stream().map(TaskMapper::mapperToTaskDtoWithoutComments).toList();
  }

  public static PaginationListDto<TaskDto> mapperToPaginationTaskDto(
      List<Task> tasks, Long totalComments) {
    if (tasks == null || totalComments == null) {
      return null;
    }

    return new PaginationListDto<>(mapperToListTaskDto(tasks), totalComments);
  }
}
