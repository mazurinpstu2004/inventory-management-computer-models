package ru.computer.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.computer.inventory.entity.Component;

@Repository
public interface ComponentRepository extends JpaRepository<Component, Long> {
}
