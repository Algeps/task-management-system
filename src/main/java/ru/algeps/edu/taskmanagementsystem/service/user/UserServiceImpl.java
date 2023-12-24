package ru.algeps.edu.taskmanagementsystem.service.user;

import static ru.algeps.edu.taskmanagementsystem.mapper.UserMapper.*;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.algeps.edu.taskmanagementsystem.dto.user.UserDto;
import ru.algeps.edu.taskmanagementsystem.model.Task;
import ru.algeps.edu.taskmanagementsystem.model.User;
import ru.algeps.edu.taskmanagementsystem.repository.UserRepository;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
  private UserRepository userRepository;
  private PasswordEncoder passwordEncoder;

  @Override
  public UserDto create(@NotNull UserDto dto) {
    dto.setPassword(passwordEncoder.encode(dto.getPassword()));
    return mapperToUserDto(userRepository.saveAndFlush(mapperToUser(dto)));
  }

  @Override
  public UserDto read(@NotNull Long userId) {
    return mapperToUserDto(userFindById(userId));
  }

  @Override
  public UserDto update(@NotNull Long userId, @NotNull UserDto dto) {
    dto.setPassword(passwordEncoder.encode(dto.getPassword()));
    User user = userFindById(userId);
    return mapperToUserDto(userRepository.saveAndFlush(updateUser(user, dto)));
  }

  @Transactional
  @Override
  public void delete(@NotNull Long userId) {
    deleteAllTaskByUserId(userId);
    userRepository.deleteById(userId);
  }

  private void deleteAllTaskByUserId(Long userId) {
    User user = userFindById(userId);
    for (Task task : user.getTasksAsExecutor()) {
      task.setUserExecutor(null);
    }
    userRepository.saveAndFlush(user);
  }

  private User userFindById(Long id) {
    return userRepository
        .findById(id)
        .orElseThrow(() -> new EntityNotFoundException("User not found with id:" + id));
  }
}
