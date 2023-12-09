package ru.algeps.edu.taskmanagementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.algeps.edu.taskmanagementsystem.model.Comment;
import ru.algeps.edu.taskmanagementsystem.model.CommentPK;

@Repository
public interface CommentRepository extends JpaRepository<Comment, CommentPK> {}
