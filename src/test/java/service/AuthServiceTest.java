package service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.computer.inventory.dto.AuthResponse;
import ru.computer.inventory.dto.LoginRequestDTO;
import ru.computer.inventory.dto.UserRequestDTO;
import ru.computer.inventory.entity.Role;
import ru.computer.inventory.entity.User;
import ru.computer.inventory.exception.RoleNotFound;
import ru.computer.inventory.exception.UserAlreadyExistsException;
import ru.computer.inventory.exception.UserNotFoundException;
import ru.computer.inventory.jwt.JwtService;
import ru.computer.inventory.repository.RoleRepository;
import ru.computer.inventory.repository.UserRepository;
import ru.computer.inventory.service.impl.AuthServiceImpl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthServiceImpl authService;

    private User testUser;

    @BeforeEach
    public void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testUser");
        testUser.setFullName("testFullname");
        testUser.setPassword("testPassword");
    }

    @Test
    public void positiveCreateTest() {
        Mockito.when(userRepository.findByUsername("testUser")).thenReturn(null);
        Mockito.when(userRepository.save(any(User.class))).thenReturn(testUser);

        User created = authService.create(testUser);

        Assertions.assertEquals("testUser", created.getUsername());
        Mockito.verify(userRepository).save(testUser);
    }

    @Test
    public void negativeCreateTest() {
        Mockito.when(userRepository.findByUsername("testUser")).thenReturn(testUser);
        Assertions.assertThrows(UserAlreadyExistsException.class, () -> authService.create(testUser));
    }

    @Test
    public void positiveReadByIdTest() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        User found = authService.readById(1L);

        Assertions.assertEquals("testUser", found.getUsername());
    }

    @Test
    public void negativeReadByIdTest() {
        Mockito.when(userRepository.findById(100L)).thenReturn(Optional.empty());
        Assertions.assertThrows(UserNotFoundException.class, () -> authService.readById(100L));
    }

    @Test
    public void readAllTest() {
        Mockito.when(userRepository.findAll()).thenReturn(Collections.singletonList(testUser));

        List<User> users = authService.readAll();

        Assertions.assertEquals(1, users.size());
        Assertions.assertEquals("testUser", users.getFirst().getUsername());
    }

    @Test
    public void positiveUpdateTest() {
        UserRequestDTO dto = new UserRequestDTO();
        dto.setUsername("newLogin");
        dto.setFullName("NewName");
        dto.setPassword("newPassword");

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        Mockito.when(passwordEncoder.encode("newPassword")).thenReturn("newEncodedPassword");
        Mockito.when(userRepository.save(any(User.class))).thenReturn(testUser);

        User updated = authService.update(1L, dto);

        Assertions.assertEquals("newLogin", updated.getUsername());
        Assertions.assertEquals("NewName", updated.getFullName());
        Assertions.assertEquals("newEncodedPassword", updated.getPassword());
        Mockito.verify(passwordEncoder).encode("newPassword");
    }

    @Test
    public void negativeUpdateTest() {
        Mockito.when(userRepository.findById(100L)).thenReturn(Optional.empty());
        Assertions.assertThrows(UserNotFoundException.class, () -> authService.update(100L, new UserRequestDTO()));
    }

    @Test
    public void positiveDeleteTest() {
        Mockito.when(userRepository.existsById(1L)).thenReturn(true);
        authService.delete(1L);
        Mockito.verify(userRepository).deleteById(1L);
    }

    @Test
    public void negativeDeleteTest() {
        Mockito.when(userRepository.existsById(100L)).thenReturn(false);
        Assertions.assertThrows(UserNotFoundException.class, () -> authService.delete(100L));
    }

    @Test
    public void positiveLoginTest() {
        LoginRequestDTO request = new LoginRequestDTO("testUser", "testPassword");

        Role role = new Role();
        role.setName("Администратор");
        testUser.setRole(role);

        Mockito.when(userRepository.findByUsername("testUser")).thenReturn(testUser);
        Mockito.when(passwordEncoder.matches("testPassword", "testPassword")).thenReturn(true);
        Mockito.when(jwtService.generateToken(Mockito.anyString(), Mockito.any(), Mockito.anyString()))
                .thenReturn("testJwt");

        AuthResponse response = authService.login(request);

        Assertions.assertEquals("testJwt", response.getToken());
        Mockito.verify(jwtService).generateToken(Mockito.anyString(), Mockito.any(), Mockito.anyString());
    }

    @Test
    public void negativeLoginInvalidPasswordTest() {
        LoginRequestDTO request = new LoginRequestDTO("testUser", "wrongPassword");

        Mockito.when(userRepository.findByUsername("testUser")).thenReturn(testUser);
        Mockito.when(passwordEncoder.matches("wrongPassword", "testPassword")).thenReturn(false);

        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () ->
                authService.login(request)
        );
        Assertions.assertEquals("Неверный пароль", exception.getMessage());
    }

    @Test
    public void positiveRegisterTest() {
        UserRequestDTO dto = new UserRequestDTO();
        dto.setUsername("newUser");
        dto.setFullName("newFullname");
        dto.setPassword("newPassword");

        Role userRole = new Role();
        userRole.setName("Пользователь");

        Mockito.when(userRepository.findByUsername("newUser")).thenReturn(null);
        Mockito.when(roleRepository.findByName("Пользователь")).thenReturn(userRole);
        Mockito.when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");
        Mockito.when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        User registered = authService.register(dto);

        Assertions.assertEquals("newUser", registered.getUsername());
        Assertions.assertEquals("encodedNewPassword", registered.getPassword());
        Assertions.assertEquals("Пользователь", registered.getRole().getName());
        Mockito.verify(userRepository).save(any(User.class));
    }

    @Test
    public void positiveUpdateRoleTest() {
        Role adminRole = new Role();
        adminRole.setName("Администратор");

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        Mockito.when(roleRepository.findByName("Администратор")).thenReturn(adminRole);

        authService.updateRole(1L, "Администратор");

        Assertions.assertEquals("Администратор", testUser.getRole().getName());
        Mockito.verify(userRepository).save(testUser);
    }

    @Test
    public void negativeUpdateRoleNotFoundTest() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        Mockito.when(roleRepository.findByName("testRole")).thenReturn(null);

        Assertions.assertThrows(RoleNotFound.class, () ->
                authService.updateRole(1L, "testRole")
        );
    }
}