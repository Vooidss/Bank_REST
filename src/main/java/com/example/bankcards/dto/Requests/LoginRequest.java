package com.example.bankcards.dto.Requests;

import lombok.Data;

@Data
public class LoginRequest {
    private String userName;
    private String password;
}
