package com.example.bankcards.dto;

import com.example.bankcards.entity.Status;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class BankCardDTO {

    private Long ownerId;

    private LocalDate expirationDate;

    private Status status;

    private BigDecimal balance;
}
