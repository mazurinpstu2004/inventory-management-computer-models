package ru.computer.inventory.service;

import ru.computer.inventory.dto.AuthResponse;
import ru.computer.inventory.dto.LoginRequestDTO;
import ru.computer.inventory.dto.UserRequestDTO;
import ru.computer.inventory.entity.User;

import java.util.List;

public interface AuthService {

    User create(User user);

    User readById(Long id);

    List<User> readAll();

    User update(Long id, UserRequestDTO userDTO);

    void delete(Long id);

    AuthResponse login(LoginRequestDTO request);

    User register(UserRequestDTO userDto);

    void updateRole(Long id, String role);
}
