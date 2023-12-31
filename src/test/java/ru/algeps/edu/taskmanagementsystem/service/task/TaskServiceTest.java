package ru.algeps.edu.taskmanagementsystem.service.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static ru.algeps.edu.taskmanagementsystem.service.task.TaskServiceImpl.NUMBER_OF_RECENT_COMMENTS_WHEN_DISPLAYING_TASK;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.algeps.edu.taskmanagementsystem.dto.*;
import ru.algeps.edu.taskmanagementsystem.dto.comment.CommentDto;
import ru.algeps.edu.taskmanagementsystem.dto.task.TaskDto;
import ru.algeps.edu.taskmanagementsystem.dto.task.TaskEditOrCreateDto;
import ru.algeps.edu.taskmanagementsystem.dto.user.UserShortInfoDto;
import ru.algeps.edu.taskmanagementsystem.enums.TaskPriority;
import ru.algeps.edu.taskmanagementsystem.enums.TaskStatus;
import ru.algeps.edu.taskmanagementsystem.model.Comment;
import ru.algeps.edu.taskmanagementsystem.model.Task;
import ru.algeps.edu.taskmanagementsystem.model.User;
import ru.algeps.edu.taskmanagementsystem.repository.CommentRepository;
import ru.algeps.edu.taskmanagementsystem.repository.TaskRepository;
import ru.algeps.edu.taskmanagementsystem.repository.UserRepository;

@Disabled
@ExtendWith(MockitoExtension.class)
class TaskServiceTest {
  @Mock private UserRepository userRepository;
  @Mock private TaskRepository taskRepository;
  @Mock private CommentRepository commentRepository;
  @InjectMocks private TaskServiceImpl taskService;

  @Test
  void givenTaskDto_whenCreateTask_thenSuccess() {
    Long userAuthorId = 1L;
    Long userExecutorId = 2L;
    User userAuthor = User.builder().userId(userAuthorId).email("user1@mail.ru").build();
    User userExecutor = User.builder().userId(userExecutorId).email("user2@mail.ru").build();
    TaskEditOrCreateDto taskDtoBefore =
        TaskEditOrCreateDto.builder()
            .header("Task")
            .description("Description task")
            .status(TaskStatus.IN_WAIT)
            .priority(TaskPriority.MEDIUM)
            .userExecutorId(userExecutor.getUserId())
            .build();
    Task taskBefore =
        Task.builder()
            .header(taskDtoBefore.getHeader())
            .description(taskDtoBefore.getDescription())
            .status(taskDtoBefore.getStatus())
            .priority(taskDtoBefore.getPriority())
            .userAuthor(userAuthor)
            .userExecutor(userExecutor)
            .build();

    Long idTask = 1L;
    OffsetDateTime offsetDateTime = OffsetDateTime.now();
    Task taskAfter =
        taskBefore.toBuilder().taskId(idTask).creationTimestamp(offsetDateTime).build();
    UserShortInfoDto userAuthorShortInfoDtoAfter =
        UserShortInfoDto.builder()
            .userId(userAuthor.getUserId())
            .email(userAuthor.getEmail())
            .build();

    when(userRepository.getReferenceById(userAuthorId)).thenReturn(userAuthor);
    when(userRepository.getReferenceById(userExecutorId)).thenReturn(userExecutor);
    when(taskRepository.saveAndFlush(taskBefore)).thenReturn(taskAfter);
    TaskDto actual = taskService.create(userAuthor.getUserId(), taskDtoBefore);

    UserShortInfoDto userExecutorShortInfoDtoAfter =
        UserShortInfoDto.builder()
            .userId(userExecutor.getUserId())
            .email(userExecutor.getEmail())
            .build();
    TaskDto taskDtoAfter =
        TaskDto.builder()
            .taskId(idTask)
            .header(taskDtoBefore.getHeader())
            .description(taskDtoBefore.getDescription())
            .status(taskDtoBefore.getStatus().getTitle())
            .priority(taskDtoBefore.getPriority().getTitle())
            .userAuthor(userAuthorShortInfoDtoAfter)
            .userExecutor(userExecutorShortInfoDtoAfter)
            .creationTimestamp(offsetDateTime)
            .fiveLastComments(
                PaginationListDto.<CommentDto>builder().list(List.of()).totalElement(0L).build())
            .build();

    assertEquals(taskDtoAfter, actual);
  }

  @Test
  void givenTaskDto_whenUpdateTask_thenSuccess() {
    Long taskId = 1L;
    Long userAuthorId = 1L;
    Long userExecutorId = 2L;
    Long userExecutorChangeId = 3L;
    User userAuthor = User.builder().userId(userAuthorId).email("user1@mail.ru").build();
    User userExecutor = User.builder().userId(userExecutorId).email("user2@mail.ru").build();
    User userExecutorChange =
        User.builder().userId(userExecutorChangeId).email("user3@mail.ru").build();
    OffsetDateTime offsetDateTime = OffsetDateTime.now();
    TaskEditOrCreateDto taskDtoBefore =
        TaskEditOrCreateDto.builder()
            .header("Task")
            .description("Description task")
            .status(TaskStatus.IN_WAIT)
            .userExecutorId(userExecutorChange.getUserId())
            .priority(TaskPriority.MEDIUM)
            .build();
    Task taskBefore =
        Task.builder()
            .taskId(taskId)
            .header("Task")
            .description("Description task2")
            .status(TaskStatus.PROGRESS)
            .priority(TaskPriority.HIGH)
            .userAuthor(userAuthor)
            .userExecutor(userExecutor)
            .creationTimestamp(offsetDateTime)
            .build();
    when(taskRepository.findById(taskId)).thenReturn(Optional.of(taskBefore));

    Task taskAfter =
        Task.builder()
            .taskId(taskId)
            .header(taskDtoBefore.getHeader())
            .description(taskDtoBefore.getDescription())
            .status(taskDtoBefore.getStatus())
            .priority(taskDtoBefore.getPriority())
            .creationTimestamp(taskBefore.getCreationTimestamp())
            .build();
    when(taskRepository.saveAndFlush(taskBefore)).thenReturn(taskAfter);

    OffsetDateTime offsetDateTimeComment = OffsetDateTime.now();
    Comment comment =
        Comment.builder()
            .task(taskBefore)
            .taskCommentId(1)
            .text("comment")
            .user(userAuthor)
            .creationTimestamp(offsetDateTimeComment)
            .build();
    List<Comment> comments = List.of(comment);
    Page<Comment> commentPage =
        new PageImpl<>(
            comments,
            PageRequest.ofSize(NUMBER_OF_RECENT_COMMENTS_WHEN_DISPLAYING_TASK),
            comments.size());
    when(commentRepository.getCommentsDescOrder(
            taskId, PageRequest.ofSize(NUMBER_OF_RECENT_COMMENTS_WHEN_DISPLAYING_TASK)))
        .thenReturn(commentPage);

    TaskDto actual = taskService.updateAsAuthor(userAuthorId, taskId, taskDtoBefore);
    UserShortInfoDto userAuthorShortInfoDto =
        UserShortInfoDto.builder()
            .userId(userAuthor.getUserId())
            .email(userAuthor.getEmail())
            .build();
    CommentDto commentDto =
        CommentDto.builder()
            .taskId(comment.getTask().getTaskId())
            .taskCommentId(comment.getTaskCommentId())
            .userAuthor(userAuthorShortInfoDto)
            .text(comment.getText())
            .creationTimestamp(comment.getCreationTimestamp())
            .build();
    List<CommentDto> commentDtos = List.of(commentDto);
    PaginationListDto<CommentDto> paginationListDto =
        PaginationListDto.<CommentDto>builder().list(commentDtos).totalElement(1L).build();
    TaskDto taskDtoAfter =
        TaskDto.builder()
            .taskId(taskId)
            .header(taskDtoBefore.getHeader())
            .description(taskDtoBefore.getDescription())
            .status(taskDtoBefore.getStatus().getTitle())
            .priority(taskDtoBefore.getPriority().getTitle())
            .creationTimestamp(taskBefore.getCreationTimestamp())
            .fiveLastComments(paginationListDto)
            .build();

    assertEquals(taskDtoAfter, actual);
  }
}
