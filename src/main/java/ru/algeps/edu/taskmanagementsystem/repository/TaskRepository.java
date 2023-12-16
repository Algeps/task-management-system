package ru.algeps.edu.taskmanagementsystem.repository;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.algeps.edu.taskmanagementsystem.model.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
  @Query(
      "select t from Task t where t.userAuthor.userId = :userId or t.userExecutor.userId = :userId")
  Page<Task> getPaginationTaskWithUsers(@NotNull Long userId, Pageable pageable);
}
