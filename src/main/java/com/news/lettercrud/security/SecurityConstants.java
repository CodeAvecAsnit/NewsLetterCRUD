package com.news.lettercrud.security;

public class SecurityConstants {

    public static final String[] PUBLIC_URLS = {
            "/login/oauth2/code/github",
            "/login/oauth2/code/google",
            "/api/v1/sign_in",
            "/api/v1/signup/**",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/api/v1/check-email",
            "/api/v1/user/posts",
            "/api/v1/news/today",
            "/api/v1/news/latest",
            "/api/v1/add/find",
            "/api/v1/auth/refresh",
            "/api/v1/auth/refresh-token"
    };

    private SecurityConstants() {
        throw new IllegalStateException("Constants class");
    }
}