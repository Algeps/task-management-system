package ru.algeps.edu.taskmanagementsystem.model;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;
import ru.algeps.edu.taskmanagementsystem.enums.TaskPriority;
import ru.algeps.edu.taskmanagementsystem.enums.TaskStatus;

@Entity
@Table(name = "tasks")
@Accessors(chain = true)
@Builder(toBuilder = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Task {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long taskId;

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

  @ManyToOne(fetch = FetchType.EAGER, optional = false)
  @JoinColumn(name = "user_author_id", nullable = false)
  private User userAuthor;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "user_executor_id")
  private User userExecutor;

  @OneToMany(
      mappedBy = "task",
      fetch = FetchType.LAZY,
      cascade = CascadeType.ALL,
      orphanRemoval = true)
  @Builder.Default
  private List<Comment> comments = new ArrayList<>();

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Task task)) return false;

    if (getTaskId() != null ? !getTaskId().equals(task.getTaskId()) : task.getTaskId() != null)
      return false;
    if (getHeader() != null ? !getHeader().equals(task.getHeader()) : task.getHeader() != null)
      return false;
    if (getDescription() != null
        ? !getDescription().equals(task.getDescription())
        : task.getDescription() != null) return false;
    if (getStatus() != task.getStatus()) return false;
    if (getPriority() != task.getPriority()) return false;
    if (getCreationTimestamp() != null
        ? !getCreationTimestamp().equals(task.getCreationTimestamp())
        : task.getCreationTimestamp() != null) return false;
    if (getUserAuthor() != null
        ? !getUserAuthor().equals(task.getUserAuthor())
        : task.getUserAuthor() != null) return false;
    return getUserExecutor() != null
        ? getUserExecutor().equals(task.getUserExecutor())
        : task.getUserExecutor() == null;
  }

  @Override
  public int hashCode() {
    int result = getTaskId() != null ? getTaskId().hashCode() : 0;
    result = 31 * result + (getHeader() != null ? getHeader().hashCode() : 0);
    result = 31 * result + (getDescription() != null ? getDescription().hashCode() : 0);
    result = 31 * result + (getStatus() != null ? getStatus().hashCode() : 0);
    result = 31 * result + (getPriority() != null ? getPriority().hashCode() : 0);
    result = 31 * result + (getCreationTimestamp() != null ? getCreationTimestamp().hashCode() : 0);
    result = 31 * result + (getUserAuthor() != null ? getUserAuthor().hashCode() : 0);
    result = 31 * result + (getUserExecutor() != null ? getUserExecutor().hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "Task{"
        + "taskId="
        + taskId
        + ", header='"
        + header
        + '\''
        + ", description='"
        + description
        + '\''
        + ", status="
        + status
        + ", priority="
        + priority
        + ", creationTimestamp="
        + creationTimestamp
        + ", userAuthor="
        + userAuthor
        + ", userExecutor="
        + userExecutor
        + '}';
  }
}
