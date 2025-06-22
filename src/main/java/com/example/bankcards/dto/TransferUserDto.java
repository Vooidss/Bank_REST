package com.example.bankcards.dto;

import com.example.bankcards.entity.transfer.TransferStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
public class TransferUserDto {
    private Long transactionId;
    private Long initiatorId;
    private Long fromCardId;
    private Long toCardId;
    private BigDecimal amount;
    private TransferStatus status;
    private Instant timestamp;
}