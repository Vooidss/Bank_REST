package com.example.bankcards.dto.Responses;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
@AllArgsConstructor
public class ApiResponse<T> {
    private final T data;
    private final String message;
    private final HttpStatus status;

    public static <T> ApiResponse<T> of(T data, String message, HttpStatus status) {
        return new ApiResponse<>(data,message, status);
    }

    public static ApiResponse<Void> of(String message, HttpStatus status) {
        return new ApiResponse<>(null, message, status);
    }
}