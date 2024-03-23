package ru.practicum.exception;

public class UnknownParamException extends RuntimeException {
    public UnknownParamException(String message) {
        super(message);
    }
}
