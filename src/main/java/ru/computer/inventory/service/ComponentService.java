package ru.computer.inventory.service;


import ru.computer.inventory.entity.Component;

import java.util.List;

public interface ComponentService {

    Component create(Component component);

    Component getById(Long id);

    List<Component> getAll();

    Component update(Long id, Component component);

    void delete(Long id);

}
