package ru.computer.inventory.service;

import ru.computer.inventory.dto.AuthResponse;
import ru.computer.inventory.dto.LoginRequest;
import ru.computer.inventory.dto.UserRequestDTO;
import ru.computer.inventory.entity.User;

import java.util.List;

public interface AuthService {

    User create(User user);

    User readById(Long id);

    List<User> readAll();

    User update(User user);

    void delete(Long id);

    AuthResponse login(LoginRequest request);

    User register(UserRequestDTO userDto);
}
