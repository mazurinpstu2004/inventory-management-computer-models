package ru.computer.inventory.exception;

public class ModelCompositionEmptyException extends BaseException {
    public ModelCompositionEmptyException(String modelName) {
      super("Невозможно запустить производство: для модели '" + modelName + "' не определен состав деталей");
    }
}
