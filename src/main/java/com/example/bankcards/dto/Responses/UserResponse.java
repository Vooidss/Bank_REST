package com.example.bankcards.dto.Responses;

import com.example.bankcards.dto.UserDTO;
import com.example.bankcards.entity.user.User;
import org.springframework.http.HttpStatus;

public class UserResponse extends Response<UserDTO>{
    public UserResponse(UserDTO user, String message, HttpStatus status) {
        super(user, message, status);
    }
}
