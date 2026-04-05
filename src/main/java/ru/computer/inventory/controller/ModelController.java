package ru.computer.inventory.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.computer.inventory.dto.ModelRequestDTO;
import ru.computer.inventory.dto.ModelResponseDTO;
import ru.computer.inventory.dto.ModelStructureDTO;
import ru.computer.inventory.dto.ModelStructureRequestDTO;
import ru.computer.inventory.entity.Model;
import ru.computer.inventory.service.impl.ModelServiceImpl;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/computer/models")
public class ModelController {

    private final ModelServiceImpl modelService;

    @Autowired
    public ModelController(ModelServiceImpl modelService) {
        this.modelService = modelService;
    }

    @PostMapping
    public Model create(@RequestBody ModelRequestDTO request) {
        Model model = new Model();
        model.setName(request.getName());
        return modelService.create(model);
    }

    @PostMapping("/{id}/components")
    public void addComponentToModel(@PathVariable Long id, @RequestBody ModelStructureRequestDTO request) {
        modelService.addComponentToModel(id, request.getComponentId(), request.getQuantity());
    }

    @GetMapping("/{id}")
    public ModelResponseDTO getModelInfo(@PathVariable Long id) {
        Model model = modelService.readById(id);
        Double cost = modelService.calculateModelCost(id);

        ModelResponseDTO response = new ModelResponseDTO();
        response.setId(model.getId());
        response.setName(model.getName());
        response.setTotalCost(cost);

        List<ModelStructureDTO> componentDto = modelService.getModelComposition(id)
                .stream()
                .map(structure -> {
                    ModelStructureDTO item = new ModelStructureDTO();
                    item.setComponentName(structure.getComponent().getName());
                    item.setQuantity(structure.getQuantity());
                    item.setPrice(structure.getComponent().getPrice());
                    return item;
                })
                .collect(Collectors.toList());

        response.setComponents(componentDto);

        return response;
    }
}
