package ru.algeps.edu.taskmanagementsystem.model;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import ru.algeps.edu.taskmanagementsystem.enums.TaskPriority;
import ru.algeps.edu.taskmanagementsystem.enums.TaskStatus;

@Entity
@Table(name = "tasks")
@Builder
@Getter
@Setter
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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Task task)) return false;

    if (getTaskId() != task.getTaskId()) return false;
    if (!getHeader().equals(task.getHeader())) return false;
    if (getDescription() != null
        ? !getDescription().equals(task.getDescription())
        : task.getDescription() != null) return false;
    if (getStatus() != task.getStatus()) return false;
    if (getPriority() != task.getPriority()) return false;
    return getCreationTimestamp().equals(task.getCreationTimestamp());
  }

  @Override
  public int hashCode() {
    int result = (int) (getTaskId() ^ (getTaskId() >>> 32));
    result = 31 * result + getHeader().hashCode();
    result = 31 * result + (getDescription() != null ? getDescription().hashCode() : 0);
    result = 31 * result + getStatus().hashCode();
    result = 31 * result + getPriority().hashCode();
    result = 31 * result + getCreationTimestamp().hashCode();
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
        + '}';
  }
}
