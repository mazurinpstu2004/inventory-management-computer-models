package ru.computer.inventory.exception;

public class RoleNotFound extends RuntimeException {
    public RoleNotFound(String roleName) {
        super("Роль " + roleName + " не найдена");
    }
}
