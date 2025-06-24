package com.example.bankcards.mappers;

import com.example.bankcards.dto.BankCardDTO;
import com.example.bankcards.entity.bankcard.BankCard;
import com.example.bankcards.entity.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;


@Mapper(componentModel = "spring")
public interface BankCardMapper {

    @Mapping(source = "owner.id", target = "ownerId")
    @Mapping(source = "id", target = "cardId")
    BankCardDTO toDto(BankCard card);

    @Mapping(source = "ownerId", target = "owner", qualifiedByName = "ownerIdToUser")
    @Mapping(source = "cardId", target = "id")
    BankCard toEntity(BankCardDTO dto);

    @Named("ownerIdToUser")
    default User ownerIdToUser(Long id) {
        if (id == null) {
            return null;
        }
        User user = new User();
        user.setId(id);
        return user;
    }
}
