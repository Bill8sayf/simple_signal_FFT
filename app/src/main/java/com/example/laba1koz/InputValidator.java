package com.example.laba1koz;

public class InputValidator {


    public static Integer validateInteger(String input, String fieldName) {
        if (input == null || input.trim().isEmpty()) {
            throw new ValidationException(fieldName + " не может быть пустым");
        }

        try {
            return Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            throw new ValidationException(fieldName + " должно быть целым числом");
        }
    }


    public static Integer validateInteger(String input, String fieldName, int min, int max) {
        Integer value = validateInteger(input, fieldName);

        if (value < min || value > max) {
            throw new ValidationException(fieldName + " должно быть от " + min + " до " + max);
        }

        return value;
    }
}