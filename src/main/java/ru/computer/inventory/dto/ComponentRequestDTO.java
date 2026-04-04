package ru.computer.inventory.dto;

import lombok.Data;

@Data
public class ComponentRequestDTO {
    private String name;
    private String category;
    private Double price;
    private Integer quantity;
}
