package com.example.bankcards.mappers;

import com.example.bankcards.dto.TransferUserDto;
import com.example.bankcards.entity.transfer.Transfer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransferMapper {

    TransferUserDto toDto(Transfer transfer);

}
