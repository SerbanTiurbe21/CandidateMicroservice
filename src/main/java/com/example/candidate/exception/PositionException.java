package com.example.candidate.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class PositionException {
    private final String message;
    private final HttpStatus httpStatus;
}
