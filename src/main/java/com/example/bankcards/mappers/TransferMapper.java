package com.example.bankcards.mappers;

import com.example.bankcards.dto.TransferUserDto;
import com.example.bankcards.entity.transfer.Transfer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransferMapper {

    @Mapping(source = "id", target = "transactionId")
    @Mapping(source = "initiator.id", target = "initiatorId")
    TransferUserDto toDto(Transfer transfer);
}
