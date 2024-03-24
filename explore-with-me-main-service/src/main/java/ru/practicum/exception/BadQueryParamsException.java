package ru.practicum.exception;

import javax.validation.ValidationException;

public class BadQueryParamsException extends ValidationException {
    public BadQueryParamsException(String message) {
        super(message);
    }
}
