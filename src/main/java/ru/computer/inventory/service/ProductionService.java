package ru.computer.inventory.service;

import ru.computer.inventory.entity.ProductionLog;

import java.util.List;

public interface ProductionService {

    ProductionLog create(ProductionLog productionLog);

    ProductionLog readById(Long id);

    List<ProductionLog> readAll();

    ProductionLog update(Long id, ProductionLog productionLog);

    void delete(Long id);

    ProductionLog registerAssembly(Long modelId);

}
