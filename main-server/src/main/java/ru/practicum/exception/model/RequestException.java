package ru.practicum.exception.model;

public class RequestException extends RuntimeException {
    public RequestException(String message) {
        super(message);
    }
}
