package ru.algeps.edu.taskmanagementsystem.service.user;

import static ru.algeps.edu.taskmanagementsystem.mapper.UserMapper.*;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.algeps.edu.taskmanagementsystem.dto.UserDto;
import ru.algeps.edu.taskmanagementsystem.model.User;
import ru.algeps.edu.taskmanagementsystem.repository.UserRepository;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
  private UserRepository userRepository;

  @Override
  public UserDto create(UserDto dto) {
    return mapperToUserDto(userRepository.saveAndFlush(mapperToUser(dto)));
  }

  @Override
  public UserDto read(Long id) {
    // todo брать потом id создателя из тела запроса
    return mapperToUserDto(
        userRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("User not found with id:" + id)));
  }

  @Override
  public UserDto update(Long id, UserDto dto) {
    User user =
        userRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("User not found with id:" + id));
    return mapperToUserDto(userRepository.saveAndFlush(updateUser(user, dto)));
  }

  @Override
  public void delete(Long id) {
    userRepository.deleteById(id);
  }
}
