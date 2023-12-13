package ru.algeps.edu.taskmanagementsystem.dto.task;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import lombok.*;
import ru.algeps.edu.taskmanagementsystem.dto.PaginationListDto;
import ru.algeps.edu.taskmanagementsystem.dto.comment.CommentDto;
import ru.algeps.edu.taskmanagementsystem.dto.user.UserShortInfoDto;
import ru.algeps.edu.taskmanagementsystem.enums.TaskPriority;
import ru.algeps.edu.taskmanagementsystem.enums.TaskStatus;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class TaskDto {
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Long taskId;

  private String header;
  private String description;
  private TaskStatus status;
  private TaskPriority priority;
  private UserShortInfoDto userAuthor;
  private UserShortInfoDto userExecutor;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private OffsetDateTime creationTimestamp;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private PaginationListDto<CommentDto> fiveLastComments;
}
