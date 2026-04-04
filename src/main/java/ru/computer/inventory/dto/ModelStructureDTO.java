package ru.computer.inventory.dto;

import lombok.Data;

@Data
public class ModelStructureDTO {
    private String componentName;
    private Integer quantity;
    private Double price;
}
