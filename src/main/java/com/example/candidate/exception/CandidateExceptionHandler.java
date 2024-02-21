package com.example.candidate.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CandidateExceptionHandler {
    @ExceptionHandler(value = {CandidateNotFoundException.class})
    public ResponseEntity<Object> handleCandidateNotFoundException(CandidateNotFoundException candidateNotFoundException) {
        CandidateException candidateException = new CandidateException(
                candidateNotFoundException.getMessage(),
                HttpStatus.NOT_FOUND
        );
        return new ResponseEntity<>(candidateException, candidateException.getHttpStatus());
    }

    @ExceptionHandler(value = {DuplicateCandidateException.class})
    public ResponseEntity<Object> handleDuplicateCandidateException(DuplicateCandidateException duplicateCandidateException) {
        CandidateException candidateException = new CandidateException(
                duplicateCandidateException.getMessage(),
                HttpStatus.BAD_REQUEST
        );
        return new ResponseEntity<>(candidateException, candidateException.getHttpStatus());
    }
}
