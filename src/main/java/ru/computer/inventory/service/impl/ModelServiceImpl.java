package ru.computer.inventory.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.computer.inventory.entity.Model;
import ru.computer.inventory.repository.ModelRepository;
import ru.computer.inventory.service.ModelService;

import java.util.List;

@Service
public class ModelServiceImpl implements ModelService {

    private final ModelRepository modelRepository;

    @Autowired
    public ModelServiceImpl(ModelRepository modelRepository) {
        this.modelRepository = modelRepository;
    }

    @Override
    @Transactional
    public Model create(Model model) {
        return modelRepository.save(model);
    }

    @Override
    public Model getById(Long id) {
        return modelRepository.findById(id).orElseThrow(() -> new RuntimeException("Model not found"));
    }

    @Override
    public List<Model> getAll() {
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
}
