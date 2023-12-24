package ru.algeps.edu.taskmanagementsystem.model;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import ru.algeps.edu.taskmanagementsystem.exceptions.CommentPKGeneratorException;

public class CommentPKGenerator implements IdentifierGenerator {

  @Override
  public Object generate(SharedSessionContractImplementor implementor, Object o) {
    if (o instanceof Comment comment) {
      int count =
          implementor
              .createSelectionQuery(
                  "select cast(count(c) as Integer) from Comment c " + "where c.task.id = :id",
                  Integer.class)
              .setParameter("id", comment.getTask().getTaskId())
              .getSingleResult();
      return ++count;
    } else {
      throw new CommentPKGeneratorException(
          "Incorrect use! CommentPKGenerator for Comment, but not " + o.getClass());
    }
  }
}
