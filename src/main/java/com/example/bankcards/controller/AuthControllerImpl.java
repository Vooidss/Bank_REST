package com.example.bankcards.controller;

import com.example.bankcards.controller.interfaces.AuthController;
import com.example.bankcards.dto.Requests.LoginRequest;
import com.example.bankcards.dto.Requests.RegisterRequest;
import com.example.bankcards.dto.Responses.Response;
import com.example.bankcards.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthControllerImpl implements AuthController {

    private final AuthService authService;

    public AuthControllerImpl(AuthService authService) {
        this.authService = authService;
    }


    @Override
    public ResponseEntity<Response> login(@RequestBody LoginRequest request){
        return authService.login(request);
    }

    @Override
    public ResponseEntity<Response> register(RegisterRequest request) {
        return authService.register(request);
    }


}
