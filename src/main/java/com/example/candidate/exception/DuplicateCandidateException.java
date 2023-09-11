package com.example.candidate.exception;

public class DuplicateCandidateException extends RuntimeException{
    public DuplicateCandidateException(String message) {
        super(message);
    }
}
