package ru.algeps.edu.taskmanagementsystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import java.util.ArrayList;
import java.util.List;
import lombok.*;

@Entity
@Table(name = "users")
@Builder
@Getter
@Setter
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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof User user)) return false;

    if (getUserId() != user.getUserId()) return false;
    if (!getLogin().equals(user.getLogin())) return false;
    if (!getEmail().equals(user.getEmail())) return false;
    return getPassword().equals(user.getPassword());
  }

  @Override
  public int hashCode() {
    int result = (int) (getUserId() ^ (getUserId() >>> 32));
    result = 31 * result + getLogin().hashCode();
    result = 31 * result + getEmail().hashCode();
    result = 31 * result + getPassword().hashCode();
    return result;
  }

  @Override
  public String toString() {
    return "User{"
        + "userId="
        + userId
        + ", login='"
        + login
        + '\''
        + ", email='"
        + email
        + '\''
        + ", password='"
        + password
        + '\''
        + '}';
  }
}
