package ru.practicum.controller;

import lombok.extern.slf4j.Slf4j;
import org.postgresql.util.PSQLException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.dto.ApiError;
import ru.practicum.exception.BadQueryParamsException;
import ru.practicum.exception.DataNotFoundException;
import ru.practicum.exception.DateTimeException;
import ru.practicum.exception.ParticipationLimitException;
import ru.practicum.exception.ParticipationRequestConflictException;
import ru.practicum.exception.StateException;
import ru.practicum.exception.UnknownParamException;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionController {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @ExceptionHandler({BadQueryParamsException.class,
            ConstraintViolationException.class,
            UnknownParamException.class,
            MethodArgumentNotValidException.class,
            IllegalStateException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handle(ValidationException e) {
        log.warn(e.getMessage(), e);
        return new ApiError(
                e.getMessage(),
                "Validation failed.",
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now().format(FORMATTER)
        );
    }

    @ExceptionHandler(DataNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handle(DataNotFoundException e) {
        log.warn(e.getMessage(), e);
        return new ApiError(
                e.getMessage(),
                "Object not found.",
                HttpStatus.NOT_FOUND,
                LocalDateTime.now().format(FORMATTER)
        );
    }

    @ExceptionHandler({DateTimeException.class,
            StateException.class,
            ParticipationRequestConflictException.class,
            ParticipationLimitException.class,
            PSQLException.class
    })
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handle(RuntimeException e) {
        log.warn(e.getMessage(), e);
        return new ApiError(
                e.getMessage(),
                "Conflating params.",
                HttpStatus.CONFLICT,
                LocalDateTime.now().format(FORMATTER)
        );
    }
}
