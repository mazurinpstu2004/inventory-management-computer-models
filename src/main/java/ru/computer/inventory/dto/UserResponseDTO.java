package ru.computer.inventory.dto;

import lombok.Data;

@Data
public class UserResponseDTO {
    private Long id;
    private String username;
    private String fullName;
    private String role;
}
