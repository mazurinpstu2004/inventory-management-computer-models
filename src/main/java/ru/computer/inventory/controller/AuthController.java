package ru.computer.inventory.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.computer.inventory.dto.AuthResponse;
import ru.computer.inventory.dto.LoginRequestDTO;
import ru.computer.inventory.dto.UserRequestDTO;
import ru.computer.inventory.dto.UserResponseDTO;
import ru.computer.inventory.entity.User;
import ru.computer.inventory.service.impl.AuthServiceImpl;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/computer/auth")
public class AuthController {

    private final AuthServiceImpl authService;

    @Autowired
    public AuthController(AuthServiceImpl authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequestDTO loginRequest) {
        return authService.login(loginRequest);
    }

    @PostMapping("/register")
    public User register(@RequestBody UserRequestDTO userDTO) {
        return authService.register(userDTO);
    }

    @PreAuthorize("hasAuthority('Администратор')")
    @GetMapping("/users")
    public List<UserResponseDTO> getAllUsers() {
        return authService.readAll()
                .stream()
                .map(user -> {
                    UserResponseDTO dto = new UserResponseDTO();
                    dto.setId(user.getId());
                    dto.setUsername(user.getUsername());
                    dto.setFullName(user.getFullName());
                    dto.setRole(user.getRole().getName());
                    return dto;
                }).collect(Collectors.toList());
    }

    @PreAuthorize("hasAuthority('Администратор')")
    @PutMapping("/users/{id}")
    public UserResponseDTO updateUser(@PathVariable Long id, @RequestBody UserRequestDTO userDTO) {
        User updated = authService.update(id, userDTO);

        UserResponseDTO response = new UserResponseDTO();
        response.setId(updated.getId());
        response.setUsername(updated.getUsername());
        response.setFullName(updated.getFullName());
        response.setRole(updated.getRole().getName());

        return response;
    }

    @PreAuthorize("hasAuthority('Администратор')")
    @PatchMapping("/users/{id}/role")
    public void changeRole(@PathVariable Long id, @RequestParam String role) {
        authService.updateRole(id, role);
    }

    @PreAuthorize("hasAuthority('Администратор')")
    @DeleteMapping("/users/{id}")
    public void delete(@PathVariable Long id) {
        authService.delete(id);
    }
}
