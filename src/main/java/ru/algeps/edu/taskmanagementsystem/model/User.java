package ru.algeps.edu.taskmanagementsystem.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.*;
import lombok.experimental.Accessors;

@Entity
@Table(name = "users")
@Accessors(chain = true)
@Builder(toBuilder = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long userId;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private String password;

  @Builder.Default
  @OneToMany(mappedBy = "userAuthor", fetch = FetchType.LAZY, orphanRemoval = true)
  private List<Task> tasksAsAuthor = new ArrayList<>();

  @Builder.Default
  @OneToMany(mappedBy = "userExecutor", fetch = FetchType.LAZY)
  private List<Task> tasksAsExecutor = new ArrayList<>();

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof User user)) return false;

    if (getUserId() != null ? !getUserId().equals(user.getUserId()) : user.getUserId() != null)
      return false;
    if (getEmail() != null ? !getEmail().equals(user.getEmail()) : user.getEmail() != null)
      return false;
    return getPassword() != null
        ? getPassword().equals(user.getPassword())
        : user.getPassword() == null;
  }

  @Override
  public int hashCode() {
    int result = getUserId() != null ? getUserId().hashCode() : 0;
    result = 31 * result + (getEmail() != null ? getEmail().hashCode() : 0);
    result = 31 * result + (getPassword() != null ? getPassword().hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "User{"
        + "userId="
        + userId
        + ", email='"
        + email
        + '\''
        + ", password='"
        + password
        + '\''
        + '}';
  }
}
