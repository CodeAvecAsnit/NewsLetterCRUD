package com.news.lettercrud.Controller;

import com.news.lettercrud.Data.DTOs.*;
import com.news.lettercrud.Data.model.BaseAccount;
import com.news.lettercrud.Data.model.RefreshToken;
import com.news.lettercrud.Security.JwtUtils;
import com.news.lettercrud.Security.RefreshTokenService;
import com.news.lettercrud.Security.UserDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/auth")
@Slf4j
public class AuthController {

    private final JwtUtils jwtUtils;
    private final RefreshTokenService refreshTokenService;
    private final HttpServletRequest request;

    @Autowired
    public AuthController(JwtUtils jwtUtils, RefreshTokenService refreshTokenService, HttpServletRequest request) {
        this.jwtUtils = jwtUtils;
        this.refreshTokenService = refreshTokenService;
        this.request = request;
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshAccessToken(@Valid @RequestBody RefreshTokenRequest request) {
        try {
            RefreshToken refreshToken = refreshTokenService.validateRefreshToken(request.getRefreshToken());
            BaseAccount user = refreshToken.getUser();
            UserDetailsImpl userDetails = UserDetailsImpl.build(user);

            String newAccessToken = jwtUtils.generateJwtTokens(userDetails);

            return ResponseEntity.ok(AuthResponse.builder()
                    .userId(user.getId())
                    .username(user.getEmail())
                    .accessToken(newAccessToken)
                    .refreshToken(request.getRefreshToken())
                    .tokenType("Bearer")
                    .expiresIn(jwtUtils.getExpirationTime() / 1000)
                    .build());

        } catch (RuntimeException e) {
            log.error("Token refresh failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("Invalid or expired refresh token", 401));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(@Valid @RequestBody RefreshTokenRequest request) {
        try {
            refreshTokenService.revokeToken(request.getRefreshToken());
            return ResponseEntity.ok(new MessageResponse("Logged out successfully"));
        } catch (Exception e) {
            log.error("Logout failed: {}", e.getMessage());
            return ResponseEntity.ok(new MessageResponse("Logout completed"));
        }
    }

    @PostMapping("/logout-all-devices")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> logoutAllDevices() {
        Long userId = SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal() instanceof BaseAccount user ? user.getId() : null;

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("User not found", 401));
        }

        refreshTokenService.revokeAllUserTokens(userId);
        return ResponseEntity.ok(new MessageResponse("Logged out from all devices"));
    }

    @GetMapping("/active-sessions")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getActiveSessions() {
        BaseAccount user = (BaseAccount) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        List<RefreshToken> activeTokens = refreshTokenService.getActiveTokensByUser(user.getId());
        List<SessionInfo> sessions = activeTokens.stream()
                .map(token -> new SessionInfo(
                        token.getId(),
                        token.getDeviceInfo(),
                        token.getCreatedAt(),
                        token.getExpiryDate()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(sessions);
    }

    private String getDeviceInfo() {
        String userAgent = request.getHeader("User-Agent");
        return userAgent != null ? userAgent : "Unknown Device";
    }
}
