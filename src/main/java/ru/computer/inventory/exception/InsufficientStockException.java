package ru.computer.inventory.exception;

public class InsufficientStockException extends BaseException {
    public InsufficientStockException(String componentName, int required, int available) {
      super(String.format("Ошибка сборки: недостаточно деталей '%s'. Требуется: %d, в наличии на складе: %d",
              componentName, required, available));
    }
}
