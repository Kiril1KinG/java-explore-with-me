package ru.practicum.Exception;

public class DateTimeValidationException extends RuntimeException {
    public DateTimeValidationException(String message) {
        super(message);
    }
}
