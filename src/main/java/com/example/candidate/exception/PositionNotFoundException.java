package com.example.candidate.exception;

public class PositionNotFoundException extends RuntimeException{
    public PositionNotFoundException(String message) {
        super(message);
    }
}
