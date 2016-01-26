package io.github.mcvlaga.client.application.validation;

/**
 * Класс помощник для валидации входных данных.
 */
public class UiValidation {

    public final static String REGEX_FOR_NUMBER = "^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$";

    public static UiValidationResult validate (String name, String number) {

        UiValidationResult result = checkIfEmpty(name, number);

        if(!number.matches(REGEX_FOR_NUMBER) && result.isValid()) {
            result.setValid(false);
            result.setMessage("The phone number format is wrong");
        }

        return result;
    }

    public static UiValidationResult checkIfEmpty(String name, String number) {
        return checkIfEmpty(new UiValidationResult(), name, number);
    }

    private static UiValidationResult checkIfEmpty(UiValidationResult result, String name, String number) {
        if (name.length() == 0){
            result.setValid(false);
            result.setMessage("Enter the name");
        }
        else if(number.length() == 0) {
            result.setValid(false);
            result.setMessage("Enter the phone number");
        }
        return result;
    }
}
