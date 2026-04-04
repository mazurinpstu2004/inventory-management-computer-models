package ru.computer.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.computer.inventory.entity.ModelStructure;

import java.util.List;

@Repository
public interface ModelStructureRepository extends JpaRepository<ModelStructure, Long> {

    List<ModelStructure> findAllByModelId(Long modelId);

    ModelStructure findByModelIdAndComponentId(Long modelId, Long componentId);
}

