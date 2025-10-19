package com.news.lettercrud.Security;


import com.news.lettercrud.Data.model.BaseAccount;
import com.news.lettercrud.Data.model.RefreshToken;
import com.news.lettercrud.Repositories.RefreshTokenRepository;
import com.news.lettercrud.Services.model.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final Logger log = LoggerFactory.getLogger(RefreshTokenService.class);

    private final RefreshTokenRepository refreshTokenRepository;

    @Qualifier("userServiceImpl")
    private final UserService userService;

    @Value("${app.jwt.refresh-token-expiration}")
    private long refreshTokenDurationMs;

    @Autowired
    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, UserService userService) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userService = userService;
    }

    public RefreshToken createRefreshToken(BaseAccount user, String deviceInfo) {
        try {
            RefreshToken refreshToken = RefreshToken.builder()
                    .token(UUID.randomUUID().toString())
                    .user(user)
                    .expiryDate(LocalDateTime.now().plusSeconds(refreshTokenDurationMs / 1000))
                    .isRevoked(false)
                    .deviceInfo(deviceInfo)
                    .build();

            return refreshTokenRepository.save(refreshToken);
        } catch (Exception e) {
            log.error("Error creating refresh token for user: {}", user.getUserId(), e);
            throw new RuntimeException("Failed to create refresh token", e);
        }
    }

    public RefreshToken validateRefreshToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Refresh token not found"));

        if (refreshToken.isExpired()) {
            refreshTokenRepository.delete(refreshToken);
            throw new RuntimeException("Refresh token has expired");
        }

        if (refreshToken.getIsRevoked()) {
            throw new RuntimeException("Refresh token has been revoked");
        }

        return refreshToken;
    }

    public void revokeToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Refresh token not found"));
        refreshToken.setIsRevoked(true);
        refreshTokenRepository.save(refreshToken);
        log.info("Refresh token revoked for user: {}", refreshToken.getUser().getUserId());
    }

    public void revokeAllUserTokens(Long userId) {
        List<RefreshToken> tokens = refreshTokenRepository.findByUserId(userId);
        tokens.forEach(token -> {
            token.setIsRevoked(true);
        });
        refreshTokenRepository.saveAll(tokens);
        log.info("All refresh tokens revoked for user: {}", userId);
    }

    public void revokeAllUserTokensExcept(Long userId, String currentToken) {
        List<RefreshToken> tokens = refreshTokenRepository.findByUserId(userId);
        tokens.stream()
                .filter(token -> !token.getToken().equals(currentToken))
                .forEach(token -> token.setIsRevoked(true));
        refreshTokenRepository.saveAll(tokens);
        log.info("All refresh tokens revoked except current for user: {}", userId);
    }

    public List<RefreshToken> getActiveTokensByUser(Long userId) {
        return refreshTokenRepository.findByUserIdAndIsRevokedFalse(userId);
    }

    @Scheduled(cron = "0 0 2 * * *")
    public void cleanupExpiredTokens() {
        try {
            refreshTokenRepository.deleteByExpiryDateBefore(LocalDateTime.now());
            log.info("Expired refresh tokens cleaned up");
        } catch (Exception e) {
            log.error("Error cleaning up expired tokens", e);
        }
    }

}
