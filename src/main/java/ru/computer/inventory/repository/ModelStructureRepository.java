package ru.computer.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.computer.inventory.entity.ModelStructure;

@Repository
public interface ModelStructureRepository extends JpaRepository<ModelStructure, Long> {
}
