package ru.algeps.edu.taskmanagementsystem.model;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import ru.algeps.edu.taskmanagementsystem.enums.TaskPriority;
import ru.algeps.edu.taskmanagementsystem.enums.TaskStatus;

@Entity
@Table(name = "tasks")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long taskId;

  @Column(nullable = false)
  private String header;

  @Column(length = 32_767)
  private String description;

  @Enumerated(value = EnumType.STRING)
  @Column(nullable = false)
  private TaskStatus status;

  @Enumerated(value = EnumType.STRING)
  @Column(nullable = false)
  private TaskPriority priority;

  @Column(nullable = false, updatable = false)
  @CreationTimestamp
  private OffsetDateTime creationTimestamp;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_author_id", nullable = false)
  private User userAuthor;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_executor_id")
  private User userExecutor;

  @OneToMany(
      mappedBy = "task",
      fetch = FetchType.LAZY,
      cascade = CascadeType.ALL,
      orphanRemoval = true)
  private List<Comment> comments = new ArrayList<>();
}
