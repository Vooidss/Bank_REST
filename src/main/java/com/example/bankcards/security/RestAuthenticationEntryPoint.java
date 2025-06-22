package com.example.bankcards.security;

import com.example.bankcards.exception.JwtAuthenticationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        String msg;
        if (authException instanceof JwtAuthenticationException) {
            msg = authException.getMessage();
        } else if (authException instanceof InsufficientAuthenticationException) {
            msg = "JWT токен отсутствует или неверный формат";
        } else {
            msg = authException.getMessage();
        }

        var body = Map.<String,Object>of(
                "message", msg,
                "code", HttpStatus.FORBIDDEN.value()
        );

        mapper.writeValue(response.getWriter(), body);
    }
}