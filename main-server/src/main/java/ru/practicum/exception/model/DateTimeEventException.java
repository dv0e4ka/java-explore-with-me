package ru.practicum.exception.model;

public class DateTimeEventException extends RuntimeException {
    public DateTimeEventException(String message) {
        super(message);
    }
}