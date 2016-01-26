package io.github.mcvlaga.client.application.validation;

/**
 * Класс, объекты которого возвращаются методами класса UiValidation,
 * используемый как результат валидации входных данных.
 */
public class UiValidationResult {

    private boolean isValid = true;

    private String message = "";

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isValid() {
        return isValid;
    }

    public String getMessage() {
        return message;
    }
}
