package com.example.bankcards.security.filters;

import com.example.bankcards.exception.JwtAuthenticationException;
import com.example.bankcards.security.JwtComponent;
import com.example.bankcards.security.SecurityConstants;
import com.example.bankcards.service.UserDetailService;
import com.example.bankcards.service.UserService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtComponent jwtComponent;
    private final UserDetailService userDetailService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = authHeader.substring(7);
        String username;
        try {
            username = jwtComponent.extractUserName(jwt);
        } catch (JwtException | IllegalArgumentException ex) {
            throw new JwtAuthenticationException("JWT токен не корректен: " + ex.getMessage());
        }

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailService.loadUserByUsername(username);

            boolean valid;
            try {
                valid = jwtComponent.isTokenValid(jwt, userDetails);
            } catch (JwtException ex) {
                throw new JwtAuthenticationException("JWT токен не корректен: " + ex.getMessage());
            }

            if (valid) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            } else {
                throw new JwtAuthenticationException("JWT токен не валиден");
            }
        }

        filterChain.doFilter(request, response);
    }
}