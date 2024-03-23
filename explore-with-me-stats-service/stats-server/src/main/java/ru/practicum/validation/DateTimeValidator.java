package ru.practicum.validation;

import org.springframework.stereotype.Component;
import ru.practicum.exception.DateTimeValidationException;

import java.time.LocalDateTime;

@Component
public class DateTimeValidator {

    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public void validate(LocalDateTime start, LocalDateTime end) {
        if (end.isBefore(start)) {
            throw new DateTimeValidationException("Incorrect DateTime, end can not be before start");
        }
    }
}
