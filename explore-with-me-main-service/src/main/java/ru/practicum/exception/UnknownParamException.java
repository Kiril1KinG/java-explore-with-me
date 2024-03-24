package ru.practicum.exception;

import javax.validation.ValidationException;

public class UnknownParamException extends ValidationException {
    public UnknownParamException(String message) {
        super(message);
    }
}
