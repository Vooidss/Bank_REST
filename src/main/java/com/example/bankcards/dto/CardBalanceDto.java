package com.example.bankcards.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CardBalanceDto {
    private Long cardId;
    private BigDecimal balance;
    private String maskedCardNumber;
    private LocalDate expirationDate;
}