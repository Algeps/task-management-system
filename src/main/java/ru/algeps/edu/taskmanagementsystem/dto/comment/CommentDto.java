package ru.algeps.edu.taskmanagementsystem.dto.comment;

import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.algeps.edu.taskmanagementsystem.dto.user.UserShortInfoDto;

@Data
@Builder
@AllArgsConstructor
public class CommentDto {
  private Long taskId;
  private int taskCommentId;
  private UserShortInfoDto userAuthor;
  private String text;
  private OffsetDateTime creationTimestamp;
}
