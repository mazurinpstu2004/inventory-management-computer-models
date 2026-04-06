package ru.computer.inventory.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.computer.inventory.entity.Component;
import ru.computer.inventory.exception.ResourceNotFoundException;
import ru.computer.inventory.repository.ComponentRepository;
import ru.computer.inventory.service.ComponentService;

import java.util.List;

@Service
public class ComponentServiceImpl implements ComponentService {

    private final ComponentRepository componentRepository;

    @Autowired
    public ComponentServiceImpl(ComponentRepository componentRepository) {
        this.componentRepository = componentRepository;
    }

    @Override
    @Transactional
    public Component create(Component component) {
        return componentRepository.save(component);
    }

    @Override
    public Component readById(Long id) {
        return componentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Компонент", id));
    }

    @Override
    public List<Component> readAll() {
        return componentRepository.findAll();
    }

    @Override
    @Transactional
    public Component update(Long id, Component component) {
        if (!componentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Компонент", id);
        }
        component.setId(id);
        return componentRepository.save(component);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!componentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Компонент", id);
        }

        componentRepository.deleteById(id);
    }

    @Override
    public void addStock(Long id, Integer amount) {
        Component component = readById(id);
        component.setQuantity(component.getQuantity() + amount);
        componentRepository.save(component);
    }

    @Override
    public List<Component> getLowStockAlerts(Integer quantity) {
        return componentRepository.findAllByQuantityLessThan(quantity);
    }

    @Override
    public List<Component> searchComponents(String name, String category, Double minPrice, Double maxPrice) {
        return componentRepository.findByFilters(name, category, minPrice, maxPrice);
    }


}
