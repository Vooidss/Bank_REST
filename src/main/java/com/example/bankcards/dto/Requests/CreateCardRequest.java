package com.example.bankcards.dto.Requests;

import com.example.bankcards.dto.BankCardDTO;
import lombok.Data;

@Data
public class CreateCardRequest {
    private Long ownerId;
}
