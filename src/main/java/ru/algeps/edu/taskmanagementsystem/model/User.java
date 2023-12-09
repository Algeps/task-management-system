package ru.algeps.edu.taskmanagementsystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long userId;

  @Column(nullable = false, unique = true)
  private String login;

  @Email
  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private String password;

  @OneToMany(mappedBy = "userAuthor", fetch = FetchType.LAZY)
  private List<Task> tasksAsAuthor = new ArrayList<>();

  @OneToMany(mappedBy = "userExecutor", fetch = FetchType.LAZY)
  private List<Task> tasksAsExecutor = new ArrayList<>();
}
