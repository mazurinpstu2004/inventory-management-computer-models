package ru.computer.inventory.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.computer.inventory.dto.ComponentRequestDTO;
import ru.computer.inventory.dto.ComponentResponseDTO;
import ru.computer.inventory.entity.Component;
import ru.computer.inventory.service.impl.ComponentServiceImpl;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/computer/components")
public class ComponentController {

    private final ComponentServiceImpl componentService;

    @Autowired
    public ComponentController(ComponentServiceImpl componentService) {
        this.componentService = componentService;
    }

    @PostMapping
    public ComponentResponseDTO create(@RequestBody ComponentRequestDTO request) {
        Component component = new Component();
        component.setName(request.getName());
        component.setCategory(request.getCategory());
        component.setPrice(request.getPrice());
        component.setQuantity(request.getQuantity());

        Component saved = componentService.create(component);

        ComponentResponseDTO response = new ComponentResponseDTO();
        response.setId(saved.getId());
        response.setName(saved.getName());
        response.setCategory(saved.getCategory());
        response.setPrice(saved.getPrice());
        response.setQuantity(saved.getQuantity());
        return response;
    }

    @GetMapping("/search")
    public List<ComponentResponseDTO> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice) {

        return componentService.searchComponents(name, category, minPrice, maxPrice)
                .stream()
                .map(entity -> {
                    ComponentResponseDTO response = new ComponentResponseDTO();
                    response.setId(entity.getId());
                    response.setName(entity.getName());
                    response.setCategory(entity.getCategory());
                    response.setPrice(entity.getPrice());
                    response.setQuantity(entity.getQuantity());
                    return response;
                }).collect(Collectors.toList());
    }

    @PatchMapping("/{id}/add")
    public void addStock(@PathVariable Long id, @RequestParam Integer amount) {
        componentService.addStock(id, amount);
    }
}
