package ru.algeps.edu.taskmanagementsystem.dto.task;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.algeps.edu.taskmanagementsystem.enums.TaskPriority;
import ru.algeps.edu.taskmanagementsystem.enums.TaskStatus;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class TaskEditOrCreateDto {
  private String header;
  private String description;
  private TaskStatus status;
  private TaskPriority priority;
  private Long userExecutorId;
}
