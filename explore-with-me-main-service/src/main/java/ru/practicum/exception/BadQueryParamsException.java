package ru.practicum.exception;

public class BadQueryParamsException extends RuntimeException {
    public BadQueryParamsException(String message) {
        super(message);
    }
}
