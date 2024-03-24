package ru.practicum.exception;

public class ParticipationRequestConflictException extends RuntimeException {
    public ParticipationRequestConflictException(String message) {
        super(message);
    }
}
