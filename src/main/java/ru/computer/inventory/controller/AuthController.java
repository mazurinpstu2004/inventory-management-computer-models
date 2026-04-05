package ru.computer.inventory.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.computer.inventory.dto.AuthResponse;
import ru.computer.inventory.dto.LoginRequest;
import ru.computer.inventory.dto.UserRequestDTO;
import ru.computer.inventory.entity.User;
import ru.computer.inventory.service.impl.AuthServiceImpl;

@RestController
@RequestMapping("/api/computer/auth")
public class AuthController {

    private final AuthServiceImpl authService;

    @Autowired
    public AuthController(AuthServiceImpl authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @PostMapping("/register")
    public User register(@RequestBody UserRequestDTO userDTO) {
        return authService.register(userDTO);
    }
}
