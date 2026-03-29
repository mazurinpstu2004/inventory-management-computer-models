package ru.computer.inventory.service;

import org.springframework.transaction.annotation.Transactional;
import ru.computer.inventory.entity.User;

import java.util.List;

public interface AuthService {

    User create(User user);

    User findById(Long id);

    List<User> findAll();

    User update(User user);

    void delete(Long id);
}
