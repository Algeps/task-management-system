package ru.algeps.edu.taskmanagementsystem.service.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.algeps.edu.taskmanagementsystem.dto.UserDto;
import ru.algeps.edu.taskmanagementsystem.model.User;
import ru.algeps.edu.taskmanagementsystem.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
  @Mock private UserRepository userRepository;
  @InjectMocks private UserServiceImpl userService;

  @Test
  void givenUserDto_whenCreateUser_thenSuccess() {
    UserDto userDtoBefore =
        UserDto.builder().login("123").email("123@mail.ru").password("123456").build();
    User userBefore = User.builder().login("123").email("123@mail.ru").password("123456").build();
    User userAfter = userBefore.toBuilder().userId(1L).build();
    UserDto userDtoAfter = userDtoBefore.toBuilder().userId(1L).build();

    when(userRepository.saveAndFlush(userBefore)).thenReturn(userAfter);
    UserDto actual = userService.create(userDtoBefore);

    assertEquals(userDtoAfter, actual);
  }

  @Test
  void givenUserDto_whenUpdateUser_thenSuccess() {
    Long id = 1L;
    UserDto userDtoBefore =
        UserDto.builder().login("123").email("123@mail.ru").password("123456").build();
    User userBefore =
        User.builder().userId(1L).login("123").email("123456@mail.ru").password("123456").build();
    User userChange = userBefore.toBuilder().email(userDtoBefore.getEmail()).build();
    User userAfter = userChange.toBuilder().build();
    UserDto userDtoAfter = userDtoBefore.toBuilder().userId(id).build();

    when(userRepository.findById(id)).thenReturn(Optional.of(userBefore));
    when(userRepository.saveAndFlush(userChange)).thenReturn(userAfter);
    UserDto actual = userService.update(id, userDtoBefore);

    assertEquals(userDtoAfter, actual);
  }
}
