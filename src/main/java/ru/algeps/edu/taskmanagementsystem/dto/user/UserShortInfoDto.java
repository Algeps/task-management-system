package ru.algeps.edu.taskmanagementsystem.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Builder(toBuilder = true)
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class UserShortInfoDto {
  private Long userId;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private String email;
}
