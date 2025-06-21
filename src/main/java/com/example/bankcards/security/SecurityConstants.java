package com.example.bankcards.security;

public final class SecurityConstants {

    private SecurityConstants() {}

    public static final String[] PUBLIC_PATHS = {
            "/api/v1/auth/**",
            "/v3/api-docs.yaml/**",
            "/v3/api-docs/**",
            "/docs/openapi.yaml",
            "/swagger-ui.html",
            "/swagger-ui/**"
    };

    public static final String[] ADMIN_PATHS = {
            "/api/v1/card/create",
            "/api/v1/card/getAll",
            "/api/v1/card/blocked/{id}",
            "/api/v1/card/activate/{id}",
            "/api/v1/card/{id}"
    };

}
