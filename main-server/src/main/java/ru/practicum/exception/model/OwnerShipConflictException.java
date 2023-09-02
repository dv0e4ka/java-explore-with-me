package ru.practicum.exception.model;

public class OwnerShipConflictException extends RuntimeException {
    public OwnerShipConflictException(String message) {
        super(message);
    }
}
