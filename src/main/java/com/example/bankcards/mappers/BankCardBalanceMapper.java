package com.example.bankcards.mappers;

import com.example.bankcards.dto.BankCardDTO;
import com.example.bankcards.dto.CardBalanceDto;
import com.example.bankcards.entity.bankcard.BankCard;
import com.example.bankcards.entity.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;


@Mapper(componentModel = "spring")
public interface BankCardBalanceMapper {

    @Mapping(source = "id", target = "cardId")
    @Mapping(source = "cardNumber", target = "maskedCardNumber")
    CardBalanceDto toDto(BankCard card);

    @Mapping(source = "cardNumber", target = "maskedCardNumber")
    CardBalanceDto toDto(BankCardDTO card);
}
