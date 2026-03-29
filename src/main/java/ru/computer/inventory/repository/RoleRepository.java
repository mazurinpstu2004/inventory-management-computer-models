package ru.computer.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.computer.inventory.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}
