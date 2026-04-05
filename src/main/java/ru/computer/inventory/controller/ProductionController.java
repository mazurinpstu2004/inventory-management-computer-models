package ru.computer.inventory.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.computer.inventory.dto.AssemblyRequestDTO;
import ru.computer.inventory.dto.ProductionLogResponseDTO;
import ru.computer.inventory.entity.ProductionLog;
import ru.computer.inventory.service.ProductionService;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/computer/production")
public class ProductionController {

    private final ProductionService productionService;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    @Autowired
    public ProductionController(ProductionService productionService) {
        this.productionService = productionService;
    }

    @PostMapping("/assemble")
    public ResponseEntity<?> assemble(@RequestBody AssemblyRequestDTO request) {
        try {
            ProductionLog productionLog = productionService.registerAssembly(request.getModelId(), request.getUserId());

            ProductionLogResponseDTO response = new ProductionLogResponseDTO();
            response.setId(productionLog.getId());
            response.setModelName(productionLog.getModel().getName());
            response.setAssembledBy(productionLog.getUser().getFullName());
            response.setDate(productionLog.getDate().format(formatter));

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/logs")
    public List<ProductionLogResponseDTO> getProductionLogs() {
        return productionService.readAll().stream().map(productionLog -> {
            ProductionLogResponseDTO response = new ProductionLogResponseDTO();
            response.setId(productionLog.getId());
            response.setModelName(productionLog.getModel().getName());
            response.setAssembledBy(productionLog.getUser().getFullName());
            response.setDate(productionLog.getDate().format(formatter));
            return response;
        }).collect(Collectors.toList());
    }
}
