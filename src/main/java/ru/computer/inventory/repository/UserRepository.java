package ru.computer.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.computer.inventory.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
