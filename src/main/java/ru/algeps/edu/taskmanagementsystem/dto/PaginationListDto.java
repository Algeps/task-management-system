package ru.algeps.edu.taskmanagementsystem.dto;

import java.util.List;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaginationListDto<T> {
  private List<T> list;
  private Long totalElement;
}
