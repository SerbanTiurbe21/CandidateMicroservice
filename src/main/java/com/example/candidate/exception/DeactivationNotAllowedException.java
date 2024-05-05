package com.example.candidate.exception;

public class DeactivationNotAllowedException extends RuntimeException{
    public DeactivationNotAllowedException(String message) {
        super(message);
    }
}
