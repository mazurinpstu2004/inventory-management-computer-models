package ru.computer.inventory.service;

import ru.computer.inventory.entity.ProductionLog;

import java.util.List;

public interface ProductionService {

    ProductionLog create(ProductionLog productionLog);

    ProductionLog getById(Long id);

    List<ProductionLog> getAll();

    ProductionLog update(Long id, ProductionLog productionLog);

    void delete(Long id);
}
