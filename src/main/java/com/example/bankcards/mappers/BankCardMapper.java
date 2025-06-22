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
    @Mapping(source = "cardNumber", target = "expirationDate")
    BankCardDTO toDto(BankCard card);

    @Mapping(source = "ownerId", target = "owner", qualifiedByName = "ownerIdToUser")
    @Mapping(source = "expirationDate", target = "cardNumber")
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
