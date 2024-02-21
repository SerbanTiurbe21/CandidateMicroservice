package com.example.candidate.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class CandidateException {
    private final String message;
    private final HttpStatus httpStatus;
}
