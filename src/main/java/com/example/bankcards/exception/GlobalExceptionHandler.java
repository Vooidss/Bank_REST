package com.example.bankcards.exception;

import com.example.bankcards.dto.Responses.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CardNotFoundException.class)
    public ResponseEntity<Response<Void>> onCardNotFound(CardNotFoundException ex) {

        HttpStatus status = ex.getStatus();

        Response<Void> payload = Response.of(ex.getReason(), status);
        return ResponseEntity
                .status(status)
                .body(payload);
    }

}
