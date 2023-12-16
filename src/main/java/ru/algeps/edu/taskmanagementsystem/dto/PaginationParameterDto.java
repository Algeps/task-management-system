package ru.algeps.edu.taskmanagementsystem.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaginationParameterDto {
  @Min(0)
  @Builder.Default
  private Integer offset = 0;

  @Min(2)
  @Max(50)
  @Builder.Default
  private Integer limit = 30;
}
