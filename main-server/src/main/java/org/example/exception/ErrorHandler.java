package org.example.exception;

import lombok.extern.slf4j.Slf4j;
import org.example.exception.model.EntityNotFoundException;
import org.example.exception.model.ErrorResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handle(EntityNotFoundException e) {
        log.error("получен статус 404 Not found {}", e.getMessage(), e);
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handle(DataIntegrityViolationException e) {
        log.error("получен статус 409 Not found {}", e.getMessage(), e);
        return new ErrorResponse(e.getMessage() + "  идет исключение уникальности ключа в бд");
    }
}
