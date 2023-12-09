package ru.algeps.edu.taskmanagementsystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "comments")
@IdClass(CommentPK.class)
@Builder
@Data
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
}
