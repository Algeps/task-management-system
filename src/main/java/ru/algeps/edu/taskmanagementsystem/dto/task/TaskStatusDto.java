package ru.algeps.edu.taskmanagementsystem.dto.task;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.algeps.edu.taskmanagementsystem.enums.TaskStatus;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class TaskStatusDto {
  private TaskStatus status;
}
