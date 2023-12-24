package ru.algeps.edu.taskmanagementsystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentPK implements Serializable {
  private Long task;

  private int taskCommentId;
}
