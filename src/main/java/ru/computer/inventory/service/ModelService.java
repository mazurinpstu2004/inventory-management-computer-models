package ru.computer.inventory.service;

import ru.computer.inventory.entity.Model;

import java.util.List;

public interface ModelService {

    Model create(Model model);

    Model getById(Long id);

    List<Model> getAll();

    Model update(Long id, Model model);

    void delete(Long id);
}
