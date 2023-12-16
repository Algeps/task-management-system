package ru.algeps.edu.taskmanagementsystem.dto.task;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.OffsetDateTime;
import lombok.*;
import ru.algeps.edu.taskmanagementsystem.dto.PaginationListDto;
import ru.algeps.edu.taskmanagementsystem.dto.comment.CommentDto;
import ru.algeps.edu.taskmanagementsystem.dto.user.UserShortInfoDto;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class TaskDto {
  private Long taskId;
  private String header;
  private String description;
  private String status;
  private String priority;
  private UserShortInfoDto userAuthor;
  private UserShortInfoDto userExecutor;
  private OffsetDateTime creationTimestamp;
  private OffsetDateTime updateTimestamp;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private PaginationListDto<CommentDto> fiveLastComments;
}
