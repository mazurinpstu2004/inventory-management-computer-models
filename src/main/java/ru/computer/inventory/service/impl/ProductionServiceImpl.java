package ru.computer.inventory.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.computer.inventory.entity.*;
import ru.computer.inventory.exception.InsufficientStockException;
import ru.computer.inventory.exception.ModelCompositionEmptyException;
import ru.computer.inventory.exception.ResourceNotFoundException;
import ru.computer.inventory.exception.UserNotFoundException;
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
        return productionLogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Запись о сборке", id));
    }

    @Override
    public List<ProductionLog> readAll() {
        return productionLogRepository.findAll();
    }

    @Override
    @Transactional
    public ProductionLog update(Long id, ProductionLog productionLog) {
        if (!productionLogRepository.existsById(id)) {
            throw new ResourceNotFoundException("Запись о сборке", id);
        }

        return productionLogRepository.save(productionLog);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!productionLogRepository.existsById(id)) {
            throw new ResourceNotFoundException("Запись о сборке", id);
        }

        productionLogRepository.deleteById(id);
    }

    @Override
    @Transactional
    public ProductionLog registerAssembly(Long modelId) {

        String login = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByUsername(login);

        if (user == null) {
            throw new UserNotFoundException(login);
        }

        Model model = modelRepository.findById(modelId).orElseThrow(()
                -> new ResourceNotFoundException("Model", modelId));

        List<ModelStructure> structures = modelStructureRepository.findAllByModelId(modelId);

        if (structures.isEmpty()) {
            throw new ModelCompositionEmptyException(model.getName());
        }

        for (ModelStructure item : structures) {
            Component component = item.getComponent();
            if (component.getQuantity() < item.getQuantity()) {
                throw new InsufficientStockException(component.getName(), item.getQuantity(), component.getQuantity());
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
