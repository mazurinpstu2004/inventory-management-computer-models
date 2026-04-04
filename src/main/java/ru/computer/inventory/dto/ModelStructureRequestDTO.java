package ru.computer.inventory.dto;

import lombok.Data;

@Data
public class ModelStructureRequestDTO {
    private Long componentId;
    private Integer quantity;
}
