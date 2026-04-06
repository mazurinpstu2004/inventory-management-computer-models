package ru.computer.inventory.exception;

public class UserNotFoundException extends BaseException {
    public UserNotFoundException(String id) {
        super("Пользователь " + id + " не найден");
    }
}
