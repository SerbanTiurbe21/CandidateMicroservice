package com.example.candidate.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class PositionExceptionHandler {
    @ExceptionHandler(value = {PositionNotFoundException.class})
    public ResponseEntity<Object> handlePositionNotFoundException(PositionNotFoundException positionNotFoundException) {
        PositionException positionException = new PositionException(
                positionNotFoundException.getMessage(),
                HttpStatus.NOT_FOUND
        );
        return new ResponseEntity<>(positionException, positionException.getHttpStatus());
    }

    @ExceptionHandler(value = {PositionAlreadyExistsException.class})
    public ResponseEntity<Object> handlePositionAlreadyExistsException(PositionAlreadyExistsException positionAlreadyExistsException) {
        PositionException positionException = new PositionException(
                positionAlreadyExistsException.getMessage(),
                HttpStatus.CONFLICT
        );
        return new ResponseEntity<>(positionException, positionException.getHttpStatus());
    }
}
