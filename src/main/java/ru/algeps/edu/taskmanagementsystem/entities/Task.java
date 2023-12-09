package ru.algeps.edu.taskmanagementsystem.entities;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import ru.algeps.edu.taskmanagementsystem.enums.TaskPriority;
import ru.algeps.edu.taskmanagementsystem.enums.TaskStatus;

@Entity
@Table(name = "tasks")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long taskId;

  @Column(nullable = false)
  private String header;

  @Column(nullable = false, length = 32_767)
  private String description;

  @Enumerated(value = EnumType.STRING)
  @Column(nullable = false)
  private TaskStatus status;

  @Enumerated(value = EnumType.STRING)
  @Column(nullable = false)
  private TaskPriority priority;

  @Column(nullable = false)
  @CreationTimestamp
  private OffsetDateTime creationTimestamp;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_author_id")
  private User userAuthor;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_executor_id")
  private User userExecutor;

  @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Comment> comments;
}
