package ru.computer.inventory.exception;

public class UserAlreadyExistsException extends BaseException {
    public UserAlreadyExistsException(String username) {
        super("Пользователь с логином " + username + " уже существует");
    }
}
