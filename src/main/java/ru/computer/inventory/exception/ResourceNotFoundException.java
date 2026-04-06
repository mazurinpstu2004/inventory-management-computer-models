package ru.computer.inventory.exception;

public class ResourceNotFoundException extends BaseException {
    public ResourceNotFoundException(String resourceName, Long id) {
      super(String.format("%s с ID %d не найден(а) в справочнике", resourceName, id));
    }
}
