package ru.practicum.exception.model;

public class PatchEventStateException extends RuntimeException {
    public PatchEventStateException(String message) {
        super(message);
    }
}