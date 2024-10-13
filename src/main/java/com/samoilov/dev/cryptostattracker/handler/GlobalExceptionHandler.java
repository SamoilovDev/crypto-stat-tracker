package com.samoilov.dev.cryptostattracker.handler;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final String BASE_VALIDATION_ERROR_MESSAGE = "Validation error";

    @ExceptionHandler({ ConstraintViolationException.class, MethodArgumentNotValidException.class })
    public void handleConstraintViolationException(ConstraintViolationException ex) {
        String errorMessage = ex.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .findFirst()
                .orElse(BASE_VALIDATION_ERROR_MESSAGE);

        throw new ResponseStatusException(BAD_REQUEST, errorMessage);
    }

}
