package ru.computer.inventory.service;

import ru.computer.inventory.entity.Model;
import ru.computer.inventory.entity.ModelStructure;

import java.util.List;

public interface ModelService {

    Model create(Model model);

    Model readById(Long id);

    List<Model> readAll();

    Model update(Long id, Model model);

    void delete(Long id);

    public void addComponentToModel(Long modelId, Long componentId, Integer quantity);

    public List<ModelStructure> getModelComposition(Long modelId);

    public Double calculateModelCost(Long modelId);
}
