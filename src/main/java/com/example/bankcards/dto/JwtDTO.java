package com.example.bankcards.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
@Builder
public class JwtDTO {
    private String token;
    private Date expirationDate;
}
