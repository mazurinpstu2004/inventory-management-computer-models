package ru.computer.inventory.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.computer.inventory.entity.Component;
import ru.computer.inventory.entity.Model;
import ru.computer.inventory.entity.ModelStructure;
import ru.computer.inventory.repository.ComponentRepository;
import ru.computer.inventory.repository.ModelRepository;
import ru.computer.inventory.repository.ModelStructureRepository;
import ru.computer.inventory.service.ModelService;

import java.util.List;

@Service
public class ModelServiceImpl implements ModelService {

    private final ModelRepository modelRepository;

    private final ModelStructureRepository modelStructureRepository;

    private final ComponentRepository componentRepository;

    @Autowired
    public ModelServiceImpl(ModelRepository modelRepository, ModelStructureRepository modelStructureRepository, ComponentRepository componentRepository) {
        this.modelRepository = modelRepository;
        this.modelStructureRepository = modelStructureRepository;
        this.componentRepository = componentRepository;
    }

    @Override
    @Transactional
    public Model create(Model model) {
        return modelRepository.save(model);
    }

    @Override
    public Model readById(Long id) {
        return modelRepository.findById(id).orElseThrow(() -> new RuntimeException("Model not found"));
    }

    @Override
    public List<Model> readAll() {
        return modelRepository.findAll();
    }

    @Override
    @Transactional
    public Model update(Long id, Model model) {
        return modelRepository.save(model);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        modelRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void addComponentToModel(Long modelId, Long componentId, Integer quantity) {
        Model model = readById(modelId);

        Component component = componentRepository.findById(componentId).orElseThrow(()
                -> new RuntimeException("Component not found"));

        ModelStructure structure = modelStructureRepository.findByModelIdAndComponentId(modelId, componentId);

        if (structure == null) {
            structure = new ModelStructure();
            structure.setModel(model);
            structure.setComponent(component);
            structure.setQuantity(quantity);
        } else {
            structure.setQuantity(structure.getQuantity() + quantity);
        }

        modelStructureRepository.save(structure);
    }

    @Override
    public List<ModelStructure> getModelComposition(Long modelId) {
        return modelStructureRepository.findAllByModelId(modelId);
    }

    @Override
    public Double calculateModelCost(Long modelId) {
        List<ModelStructure> structures = modelStructureRepository.findAllByModelId(modelId);

        return structures.stream()
                .mapToDouble(s -> s.getComponent().getPrice() * s.getQuantity())
                .sum();
    }
}
