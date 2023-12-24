package ru.algeps.edu.taskmanagementsystem.service.user;

import org.springframework.stereotype.Service;
import ru.algeps.edu.taskmanagementsystem.dto.user.UserDto;

@Service
public interface UserService {
    UserDto create(UserDto dto);

    UserDto read(Long id);

    UserDto update(Long id, UserDto dto);

    void delete(Long id);
}
