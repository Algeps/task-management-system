package ru.algeps.edu.taskmanagementsystem.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.algeps.edu.taskmanagementsystem.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> getByEmail(String login);
}
