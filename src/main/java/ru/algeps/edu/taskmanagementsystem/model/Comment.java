package ru.algeps.edu.taskmanagementsystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.OffsetDateTime;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "comments")
@IdClass(CommentPK.class)
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
  @Id
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "task_id", nullable = false, updatable = false)
  private Task task;

  @Id
  @GeneratedValue(generator = "CommentPKGenerator")
  @GenericGenerator(name = "CommentPKGenerator", type = CommentPKGenerator.class)
  private int taskCommentId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_author_id", nullable = false, updatable = false)
  private User user;

  @NotBlank
  @Column(nullable = false, length = 32_767)
  private String text;

  @Column(nullable = false, updatable = false)
  @CreationTimestamp
  private OffsetDateTime creationTimestamp;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Comment comment)) return false;

    if (getTaskCommentId() != comment.getTaskCommentId()) return false;
    if (!getText().equals(comment.getText())) return false;
    return getCreationTimestamp().equals(comment.getCreationTimestamp());
  }

  @Override
  public int hashCode() {
    int result = getTaskCommentId();
    result = 31 * result + getText().hashCode();
    result = 31 * result + getCreationTimestamp().hashCode();
    return result;
  }

  @Override
  public String toString() {
    return "Comment{"
        + "taskCommentId="
        + taskCommentId
        + ", text='"
        + text
        + '\''
        + ", creationTimestamp="
        + creationTimestamp
        + '}';
  }
}
