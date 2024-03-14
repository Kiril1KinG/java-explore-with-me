package ru.practicum.validation;

import ru.practicum.Exception.DateTimeValidationException;

import java.time.LocalDateTime;

public class DateTimeValidator {

    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public void validate(LocalDateTime start, LocalDateTime end) {
        if (end.isBefore(start)) {
            throw new DateTimeValidationException("Incorrect DateTime, end can nor be before start");
        }
    }
}
