package ru.computer.inventory.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.computer.inventory.dto.AuthResponse;
import ru.computer.inventory.dto.LoginRequest;
import ru.computer.inventory.dto.UserRequestDTO;
import ru.computer.inventory.entity.Role;
import ru.computer.inventory.entity.User;
import ru.computer.inventory.jwt.JwtService;
import ru.computer.inventory.repository.RoleRepository;
import ru.computer.inventory.repository.UserRepository;
import ru.computer.inventory.service.AuthService;

import java.util.List;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    @Transactional
    public User create(User user) {
        return userRepository.save(user);
    }

    @Override
    public User readById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public List<User> readAll() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public User update(Long id, UserRequestDTO dto) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        existingUser.setUsername(dto.getUsername());
        existingUser.setFullName(dto.getFullName());

        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        return userRepository.save(existingUser);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername());

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Incorrect password");
        }

        String token = jwtService.generateToken(
                user.getUsername(),
                user.getId(),
                user.getRole().getName()
        );

        return new AuthResponse(token);
    }

    @Override
    public User register(UserRequestDTO userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setFullName(userDto.getFullName());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRole(roleRepository.findByName("Пользователь"));

        return userRepository.save(user);
    }

    @Override
    public void updateRole(Long id, String roleName) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

        Role role = roleRepository.findByName(roleName);

        if (role == null) {
            throw new RuntimeException("Role not found");
        }

        user.setRole(role);
        userRepository.save(user);
    }
}
