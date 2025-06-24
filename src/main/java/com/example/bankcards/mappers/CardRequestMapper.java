package com.example.bankcards.mappers;

import com.example.bankcards.dto.CardRequestDTO;
import com.example.bankcards.entity.bankcard.BankCard;
import com.example.bankcards.entity.block.CardRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CardRequestMapper {

    CardRequestDTO toDto(CardRequest blockRequest);

    BankCard toEntity(CardRequestDTO dto);
}
