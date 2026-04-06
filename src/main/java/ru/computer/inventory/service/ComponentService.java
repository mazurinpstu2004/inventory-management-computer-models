package ru.computer.inventory.service;

import ru.computer.inventory.entity.Component;

import java.util.List;

public interface ComponentService {

    Component create(Component component);

    Component readById(Long id);

    List<Component> readAll();

    Component update(Long id, Component component);

    void delete(Long id);

    void addStock(Long id, Integer amount);

    List<Component> searchComponents(String name, String category, Double minPrice, Double maxPrice);
}
