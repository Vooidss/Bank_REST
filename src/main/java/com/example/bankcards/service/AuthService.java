package com.example.bankcards.service;

import com.example.bankcards.dto.JwtDTO;
import com.example.bankcards.dto.Requests.LoginRequest;
import com.example.bankcards.dto.Requests.RegisterRequest;
import com.example.bankcards.dto.Responses.Response;
import com.example.bankcards.dto.Responses.JwtResponse;
import com.example.bankcards.entity.user.User;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.security.JwtComponent;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;

@Service
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

    public ResponseEntity<Response> login(LoginRequest request){


        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword())
            );

        }catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Response.of("Неверный логин или пароль", HttpStatus.NOT_FOUND)
            );
        }

        final String JWT = jwtComponent.generateJwtToken(request.getUserName());
        final Date expiration = jwtComponent.extractExpiration(JWT);
        final JwtDTO JwtDto = JwtDTO.builder()
                .token(JWT)
                .expirationDate(expiration)
                .build();

        return ResponseEntity.ok().body(
                new JwtResponse(JwtDto, "Пользователь вошёл успешно!", HttpStatus.OK)
        );
    }

    public ResponseEntity<Response> register(RegisterRequest request) {
        if(request.getUserName() == null && request.getPassword() == null){

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    Response.of("Не хватает либо логина либо пароля!", HttpStatus.BAD_REQUEST)
            );
        }

        User user = User.builder()
                .username(request.getUserName())
                .password(passwordEncoder.encode(request.getPassword()))
                .bankCartList(Collections.emptyList())
                .build();

        userRepository.save(user);

        final String JWT = jwtComponent.generateJwtToken(request.getUserName());

        return ResponseEntity.ok().body(
                new JwtResponse(JwtDTO.builder()
                        .token(JWT)
                        .expirationDate(jwtComponent.extractExpiration(JWT))
                        .build(),
                        "Пользователь успешно зарегистрирован!",
                        HttpStatus.OK)
        );

    }
}
