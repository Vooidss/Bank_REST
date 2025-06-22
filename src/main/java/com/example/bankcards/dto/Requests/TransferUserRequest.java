package com.example.bankcards.dto.Requests;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferUserRequest {
    private Long fromCardId;
    private Long toCardId;
    private BigDecimal amount;
}
