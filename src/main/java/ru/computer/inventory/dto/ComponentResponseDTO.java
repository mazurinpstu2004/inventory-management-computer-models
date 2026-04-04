package ru.computer.inventory.dto;

import lombok.Data;

@Data
public class ComponentResponseDTO {
    private Long id;
    private String name;
    private String category;
    private Double price;
    private Long quantity;
}
