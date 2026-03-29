package ru.computer.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.computer.inventory.entity.ProductionLog;

@Repository
public interface ProductionLogRepository extends JpaRepository<ProductionLog, Long> {
}
