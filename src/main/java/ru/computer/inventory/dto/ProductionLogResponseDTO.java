package ru.computer.inventory.dto;

import lombok.Data;

@Data
public class ProductionLogResponseDTO {
    private Long id;
    private String modelName;
    private String assembledBy;
    private String date;
}
