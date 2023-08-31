package ru.practicum.exception.model;

public class EntityNoFoundException extends RuntimeException {
    public EntityNoFoundException(String message) {
        super(message);
    }
}
