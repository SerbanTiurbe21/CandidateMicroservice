package com.example.candidate.exception;

public class PositionAlreadyExistsException extends RuntimeException{
    public PositionAlreadyExistsException(String message) {
        super(message);
    }
}
