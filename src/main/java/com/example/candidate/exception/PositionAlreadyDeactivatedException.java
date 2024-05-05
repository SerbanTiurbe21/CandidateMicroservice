package com.example.candidate.exception;

public class PositionAlreadyDeactivatedException extends RuntimeException{
    public PositionAlreadyDeactivatedException(String message) {
        super(message);
    }
}
