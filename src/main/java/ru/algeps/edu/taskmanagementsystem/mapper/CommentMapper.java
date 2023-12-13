package ru.algeps.edu.taskmanagementsystem.mapper;

import java.util.Collections;
import java.util.List;
import ru.algeps.edu.taskmanagementsystem.dto.comment.CommentDto;
import ru.algeps.edu.taskmanagementsystem.dto.PaginationListDto;
import ru.algeps.edu.taskmanagementsystem.model.Comment;
import ru.algeps.edu.taskmanagementsystem.model.Task;
import ru.algeps.edu.taskmanagementsystem.model.User;

public class CommentMapper {
  private CommentMapper() {}

  public static CommentDto mapperToCommentDto(Comment comment) {
    if (comment == null) {
      return null;
    }

    return CommentDto.builder()
        .taskId(comment.getTask().getTaskId())
        .taskCommentId(comment.getTaskCommentId())
        .text(comment.getText())
        .creationTimestamp(comment.getCreationTimestamp())
        .userAuthor(UserMapper.mapperToUserShortInfoDto(comment.getUser()))
        .build();
  }

  public static List<CommentDto> mapperToListCommentDto(List<Comment> comments) {
    if (comments == null) {
      return Collections.emptyList();
    }

    return comments.stream().map(CommentMapper::mapperToCommentDto).toList();
  }

  public static PaginationListDto<CommentDto> mapperToPaginationListCommentDto(
      List<Comment> comments, long totalComments) {
    if (comments == null) {
      return null;
    }

    return new PaginationListDto<>(mapperToListCommentDto(comments), totalComments);
  }

  public static Comment mapperToComment(CommentDto dto, Task task, User userAuthor) {
    return Comment.builder().task(task).user(userAuthor).text(dto.getText()).build();
  }
}
