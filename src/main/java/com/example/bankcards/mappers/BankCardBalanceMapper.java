package com.example.bankcards.mappers;

import com.example.bankcards.dto.BankCardDTO;
import com.example.bankcards.dto.CardBalanceDto;
import com.example.bankcards.entity.bankcard.BankCard;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface BankCardBalanceMapper {
    CardBalanceDto toDto(BankCard card);
    CardBalanceDto toDto(BankCardDTO card);
}
