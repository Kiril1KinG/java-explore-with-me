package ru.practicum.exception;

public class ParticipationLimitException extends RuntimeException {
    public ParticipationLimitException(String message) {
        super(message);
    }
}
