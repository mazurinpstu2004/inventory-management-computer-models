package ru.computer.inventory.dto;

import lombok.Data;

import java.util.List;

@Data
public class ModelResponseDTO {
    private Long id;
    private String name;
    private Double totalCost;
    private List<ModelStructureDTO> components;
}
