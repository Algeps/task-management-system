package ru.algeps.edu.taskmanagementsystem.mapper;

import ru.algeps.edu.taskmanagementsystem.dto.user.UserDto;
import ru.algeps.edu.taskmanagementsystem.dto.user.UserShortInfoDto;
import ru.algeps.edu.taskmanagementsystem.model.User;

public class UserMapper {
  private UserMapper() {}

  public static User mapperToUser(UserDto dto) {
    if (dto == null) {
      return null;
    }

    return User.builder().email(dto.getEmail()).password(dto.getPassword()).build();
  }

  public static User updateUser(User user, UserDto dto) {
    if (dto == null) {
      return null;
    }

    if (dto.getEmail() != null) user.setEmail(dto.getEmail());
    if (dto.getPassword() != null) user.setPassword(dto.getPassword());
    return user;
  }

  public static UserDto mapperToUserDto(User user) {
    if (user == null) {
      return null;
    }

    return UserDto.builder().email(user.getEmail()).password(user.getPassword()).build();
  }

  public static UserShortInfoDto mapperToUserShortInfoDto(User user) {
    if (user == null) {
      return null;
    }

    return UserShortInfoDto.builder().userId(user.getUserId()).email(user.getEmail()).build();
  }
}
