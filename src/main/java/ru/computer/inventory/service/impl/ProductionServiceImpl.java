package ru.computer.inventory.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.computer.inventory.entity.ProductionLog;
import ru.computer.inventory.repository.ProductionLogRepository;
import ru.computer.inventory.service.ProductionService;

import java.util.List;

@Service
public class ProductionServiceImpl implements ProductionService {

    private final ProductionLogRepository productionLogRepository;

    @Autowired
    public ProductionServiceImpl(ProductionLogRepository productionLogRepository) {
        this.productionLogRepository = productionLogRepository;
    }

    @Override
    @Transactional
    public ProductionLog create(ProductionLog productionLog) {
        return productionLogRepository.save(productionLog);
    }

    @Override
    public ProductionLog getById(Long id) {
        return productionLogRepository.findById(id).orElseThrow(() -> new RuntimeException("ProductionLog not found"));
    }

    @Override
    public List<ProductionLog> getAll() {
        return productionLogRepository.findAll();
    }

    @Override
    public ProductionLog update(Long id, ProductionLog productionLog) {
        return productionLogRepository.save(productionLog);
    }

    @Override
    public void delete(Long id) {
        productionLogRepository.deleteById(id);
    }
}
