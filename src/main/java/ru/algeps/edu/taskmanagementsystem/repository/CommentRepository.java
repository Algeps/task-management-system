package ru.algeps.edu.taskmanagementsystem.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.algeps.edu.taskmanagementsystem.model.Comment;
import ru.algeps.edu.taskmanagementsystem.model.CommentPK;

@Repository
public interface CommentRepository extends JpaRepository<Comment, CommentPK> {
  /** Возвращает указанное количество последних комментариев. */
  @Query(
      "SELECT c FROM Comment c "
          + "WHERE c.task.taskId = :taskId "
          + "ORDER BY c.taskCommentId desc")
  Page<Comment> getCommentsDescOrder(Long taskId, Pageable pageable);
}
