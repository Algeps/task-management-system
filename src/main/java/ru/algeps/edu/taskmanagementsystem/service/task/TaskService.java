package ru.algeps.edu.taskmanagementsystem.service.task;

import org.springframework.stereotype.Service;
import ru.algeps.edu.taskmanagementsystem.dto.PaginationListDto;
import ru.algeps.edu.taskmanagementsystem.dto.PaginationParameterDto;
import ru.algeps.edu.taskmanagementsystem.dto.task.TaskDto;
import ru.algeps.edu.taskmanagementsystem.dto.task.TaskEditOrCreateDto;
import ru.algeps.edu.taskmanagementsystem.dto.task.TaskStatusDto;
import ru.algeps.edu.taskmanagementsystem.enums.TaskSort;

@Service
public interface TaskService {
  TaskDto create(Long userAuthorI, TaskEditOrCreateDto dto);

  TaskDto read(Long taskId);

  PaginationListDto<TaskDto> readAllPagination(
      Long userId, PaginationParameterDto parameter, TaskSort taskSort);

  TaskDto updateAsAuthor(Long userAuthorId, Long taskId, TaskEditOrCreateDto dto);

  TaskDto updateAsExecutor(Long userExecutorId, Long taskId, TaskStatusDto dto);

  void delete(Long userId, Long taskId);
}
