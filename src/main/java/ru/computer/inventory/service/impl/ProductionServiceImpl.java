package ru.computer.inventory.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.computer.inventory.entity.*;
import ru.computer.inventory.repository.*;
import ru.computer.inventory.service.ProductionService;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductionServiceImpl implements ProductionService {

    private final ProductionLogRepository productionLogRepository;

    private final ModelRepository modelRepository;

    private final ModelStructureRepository modelStructureRepository;

    private final ComponentRepository componentRepository;

    private final UserRepository userRepository;

    @Autowired
    public ProductionServiceImpl(ProductionLogRepository productionLogRepository, ModelRepository modelRepository, ModelRepository modelRepository1, ModelStructureRepository modelStructureRepository, ComponentRepository componentRepository, UserRepository userRepository) {
        this.productionLogRepository = productionLogRepository;
        this.modelRepository = modelRepository1;
        this.modelStructureRepository = modelStructureRepository;
        this.componentRepository = componentRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public ProductionLog create(ProductionLog productionLog) {
        return productionLogRepository.save(productionLog);
    }

    @Override
    public ProductionLog readById(Long id) {
        return productionLogRepository.findById(id).orElseThrow(() -> new RuntimeException("ProductionLog not found"));
    }

    @Override
    public List<ProductionLog> readAll() {
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

    @Override
    @Transactional
    public ProductionLog registerAssembly(Long modelId, Long userId) {

        Model model = modelRepository.findById(modelId).orElseThrow(() -> new RuntimeException("Model not found"));

        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        List<ModelStructure> structures = modelStructureRepository.findAllByModelId(modelId);

        if (structures.isEmpty()) {
            throw new RuntimeException("Model not found");
        }

        for (ModelStructure item : structures) {
            Component component = item.getComponent();
            if (component.getQuantity() < item.getQuantity()) {
                throw new RuntimeException("Component not enough");
            }
        }

        for (ModelStructure item : structures) {
            Component component = item.getComponent();
            component.setQuantity(component.getQuantity() - item.getQuantity());
            componentRepository.save(component);
        }

        ProductionLog productionLog = new ProductionLog();
        productionLog.setModel(model);
        productionLog.setUser(user);
        productionLog.setDate(LocalDateTime.now());

        return productionLogRepository.save(productionLog);
    }
}
