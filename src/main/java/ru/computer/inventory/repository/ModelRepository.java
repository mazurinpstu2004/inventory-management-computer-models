package ru.computer.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.computer.inventory.entity.Model;

@Repository
public interface ModelRepository extends JpaRepository<Model, Long> {
}
