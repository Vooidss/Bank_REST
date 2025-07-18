package com.example.bankcards.exception.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForbiddenException extends ApiException {
    public ForbiddenException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }
}