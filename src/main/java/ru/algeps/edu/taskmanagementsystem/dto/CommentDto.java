package ru.algeps.edu.taskmanagementsystem.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CommentDto {
  private Long taskId;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private int taskCommentId;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private UserShortInfoDto userAuthor;

  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private Long userAuthorId;

  private String text;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private OffsetDateTime creationTimestamp;
}
