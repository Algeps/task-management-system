package ru.algeps.edu.taskmanagementsystem.service.task;

import org.springframework.stereotype.Service;
import ru.algeps.edu.taskmanagementsystem.dto.TaskDto;
import ru.algeps.edu.taskmanagementsystem.dto.TaskEditOrCreateDto;

@Service
public interface TaskService {
  TaskDto create(TaskEditOrCreateDto dto);

  TaskDto read(Long id);

  TaskDto update(Long id, TaskEditOrCreateDto dto);

  void delete(Long id);
}
