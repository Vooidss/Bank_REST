package com.example.bankcards.dto.Responses;

import com.example.bankcards.dto.JwtDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

public class JwtResponse extends ApiResponse<JwtDTO> {

    public JwtResponse(JwtDTO token, String message, HttpStatus status) {
        super(token, message, status);
    }

}