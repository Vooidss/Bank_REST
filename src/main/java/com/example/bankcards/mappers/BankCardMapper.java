package com.example.bankcards.mappers;

import com.example.bankcards.dto.BankCardDTO;
import com.example.bankcards.entity.BankCard;
import com.example.bankcards.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;


@Mapper(componentModel = "spring")
public interface BankCardMapper {

    @Mapping(source = "owner.id", target = "ownerId")
    BankCardDTO toDto(BankCard card);

    @Mapping(source = "ownerId", target = "owner", qualifiedByName = "ownerIdToUser")
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
