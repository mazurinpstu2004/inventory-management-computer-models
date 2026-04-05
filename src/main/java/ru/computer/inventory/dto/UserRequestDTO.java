package ru.computer.inventory.dto;

import lombok.Data;

@Data
public class UserRequestDTO {
    private String username;
    private String fullName;
    private String password;
}
