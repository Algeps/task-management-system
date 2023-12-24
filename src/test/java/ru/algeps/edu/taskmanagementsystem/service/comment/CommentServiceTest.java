package ru.algeps.edu.taskmanagementsystem.service.comment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.time.OffsetDateTime;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.algeps.edu.taskmanagementsystem.dto.comment.CommentCreateDto;
import ru.algeps.edu.taskmanagementsystem.dto.comment.CommentDto;
import ru.algeps.edu.taskmanagementsystem.dto.user.UserShortInfoDto;
import ru.algeps.edu.taskmanagementsystem.model.Comment;
import ru.algeps.edu.taskmanagementsystem.model.Task;
import ru.algeps.edu.taskmanagementsystem.model.User;
import ru.algeps.edu.taskmanagementsystem.repository.CommentRepository;
import ru.algeps.edu.taskmanagementsystem.repository.TaskRepository;
import ru.algeps.edu.taskmanagementsystem.repository.UserRepository;

@Disabled
@ExtendWith(MockitoExtension.class)
class CommentServiceTest {
  @Mock private CommentRepository commentRepository;
  @Mock private UserRepository userRepository;
  @Mock private TaskRepository taskRepository;
  @InjectMocks private CommentServiceImpl commentService;

  @Test
  void givenCommentDto_whenCreateComment_thenSuccess() {
    Long idTask = 1L;
    Long userAuthorId = 1L;
    User userAuthor = User.builder().userId(userAuthorId).email("user@mail.ru").build();
    Task task = Task.builder().taskId(idTask).build();
    String textComment = "Simple Text";
    CommentCreateDto commentDtoBefore = new CommentCreateDto(idTask, textComment);
    Comment commentBefore =
        Comment.builder().task(task).user(userAuthor).text(commentDtoBefore.text()).build();
    Integer idCommentOfTask = 1;
    OffsetDateTime offsetDateTime = OffsetDateTime.now();
    Comment commentAfter =
        Comment.builder()
            .task(task)
            .user(userAuthor)
            .taskCommentId(idCommentOfTask)
            .creationTimestamp(offsetDateTime)
            .text(commentBefore.getText())
            .build();

    when(taskRepository.getReferenceById(idTask)).thenReturn(task);
    when(userRepository.getReferenceById(userAuthorId)).thenReturn(userAuthor);
    when(commentRepository.saveAndFlush(commentBefore)).thenReturn(commentAfter);
    CommentDto actual = commentService.create(commentDtoBefore, userAuthorId);
    UserShortInfoDto userShortInfoDto =
        UserShortInfoDto.builder()
            .userId(userAuthor.getUserId())
            .email(userAuthor.getEmail())
            .build();
    CommentDto commentDtoAfter =
        CommentDto.builder()
            .taskId(commentAfter.getTask().getTaskId())
            .taskCommentId(commentAfter.getTaskCommentId())
            .text(commentAfter.getText())
            .creationTimestamp(commentAfter.getCreationTimestamp())
            .userAuthor(userShortInfoDto)
            .build();

    assertEquals(commentDtoAfter, actual);
  }
}
