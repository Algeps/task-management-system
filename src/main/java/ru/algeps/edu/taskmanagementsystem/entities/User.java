package ru.algeps.edu.taskmanagementsystem.entities;

import jakarta.persistence.*;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
  @Id private long userId;
  private String login;
  private String email;
  private String password;

  @OneToMany(mappedBy = "userAuthor", fetch = FetchType.LAZY)
  private List<Task> tasksAsAuthor;

  @OneToMany(mappedBy = "userExecutor", fetch = FetchType.LAZY)
  private List<Task> tasksAsExecutor;

  public void addTaskAsAuthor(Task task) {
    tasksAsAuthor.add(task);
  }

  public void addTaskAsExecutor(Task task) {
    tasksAsExecutor.add(task);
  }
}
