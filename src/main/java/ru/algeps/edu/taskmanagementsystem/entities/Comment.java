package ru.algeps.edu.taskmanagementsystem.entities;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "comments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
  @EmbeddedId private CommentPK id = new CommentPK();

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("taskId")
  @JoinColumn(name = "task_id")
  private Task task;

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("userAuthorId")
  @JoinColumn(name = "user_author_id")
  private Task user;

  @Column(nullable = false, length = 32_767)
  private String text;

  @Column(nullable = false)
  @CreationTimestamp
  private OffsetDateTime creationTimestamp;
}

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
class CommentPK implements Serializable {
  @Column(nullable = false, updatable = false)
  private Long taskId;

  @Column(nullable = false, updatable = false)
  private Long taskCommentId;

  @Column(nullable = false, updatable = false)
  private Long userAuthorId;
}
