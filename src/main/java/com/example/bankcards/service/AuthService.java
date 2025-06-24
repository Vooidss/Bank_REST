package com.example.bankcards.service;

import com.example.bankcards.dto.JwtDTO;
import com.example.bankcards.dto.Requests.LoginRequest;
import com.example.bankcards.dto.Requests.RegisterRequest;
import com.example.bankcards.dto.Responses.Response;
import com.example.bankcards.dto.Responses.JwtResponse;
import com.example.bankcards.entity.user.User;
import com.example.bankcards.exception.InvalidCredentialsException;
import com.example.bankcards.exception.MissingCredentialsException;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.security.JwtComponent;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;

@Service
@Slf4j
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtComponent jwtComponent;
    private final PasswordEncoder passwordEncoder;

    public AuthService(AuthenticationManager authenticationManager, UserRepository userRepository, JwtComponent jwtComponent, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtComponent = jwtComponent;
        this.passwordEncoder = passwordEncoder;
    }

    public JwtDTO login(LoginRequest request){
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword())
            );

        }catch (AuthenticationException e) {
            throw new InvalidCredentialsException("Неверные логин или пароль");
        }

        final String JWT = jwtComponent.generateJwtToken(request.getUserName());
        final Date expiration = jwtComponent.extractExpiration(JWT);

        return JwtDTO.builder()
                .token(JWT)
                .expirationDate(expiration)
                .build();
    }

    public JwtDTO register(RegisterRequest request) {
        if(request.getUserName() == null && request.getPassword() == null){
            throw new MissingCredentialsException("Отстутвует логин или пароль");
        }

        User user = User.builder()
                .username(request.getUserName())
                .password(passwordEncoder.encode(request.getPassword()))
                .bankCartList(Collections.emptyList())
                .build();

        userRepository.save(user);

        final String JWT = jwtComponent.generateJwtToken(request.getUserName());

        return JwtDTO.builder()
                        .token(JWT)
                        .expirationDate(jwtComponent.extractExpiration(JWT))
                        .build();

    }
}
