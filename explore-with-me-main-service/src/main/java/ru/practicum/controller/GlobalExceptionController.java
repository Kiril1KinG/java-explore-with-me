package ru.practicum.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionController {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handle(BadQueryParamsException e) {
        log.warn(e.getMessage(), e);
        return new ApiError(
                e.getMessage(),
                "Validation for input object failed.",
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now().format(FORMATTER)
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handle(ConstraintViolationException e) {
        log.warn(e.getMessage(), e);
        return new ApiError(
                e.getMessage(),
                "Validation for input object failed.",
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now().format(FORMATTER)
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handle(UnknownParamException e) {
        log.warn(e.getMessage(), e);
        return new ApiError(
                e.getMessage(),
                "Unknown parameter.",
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now().format(FORMATTER)
        );
    }

    @ExceptionHandler
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

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handle(DateTimeException e) {
        log.warn(e.getMessage(), e);
        return new ApiError(
                e.getMessage(),
                "Incorrect datetime.",
                HttpStatus.CONFLICT,
                LocalDateTime.now().format(FORMATTER)
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handle(StateException e) {
        log.warn(e.getMessage(), e);
        return new ApiError(
                e.getMessage(),
                "State error.",
                HttpStatus.CONFLICT,
                LocalDateTime.now().format(FORMATTER)
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handle(ParticipationRequestConflictException e) {
        log.warn(e.getMessage(), e);
        return new ApiError(
                e.getMessage(),
                "State error.",
                HttpStatus.CONFLICT,
                LocalDateTime.now().format(FORMATTER)
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handle(ParticipationLimitException e) {
        log.warn(e.getMessage(), e);
        return new ApiError(
                e.getMessage(),
                "Participation limit error.",
                HttpStatus.CONFLICT,
                LocalDateTime.now().format(FORMATTER)
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handle(SQLIntegrityConstraintViolationException e) {
        log.warn(e.getMessage(), e);
        return new ApiError(
                e.getMessage(),
                "Integrity constraint exception",
                HttpStatus.CONFLICT,
                LocalDateTime.now().format(FORMATTER)
        );
    }

}
