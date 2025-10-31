package com.news.lettercrud.data.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author :Asnit Bakhati
 */

@Setter
@Getter
@Entity
@Table(name = "refresh_tokens")
@Builder
public class RefreshToken{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private LocalDateTime expiryDate;

    @Column(nullable = false)
    private Boolean isRevoked;

    @Column(nullable = false)
    private String deviceInfo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private BaseAccount user;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public RefreshToken() {
    }

    public RefreshToken(int id, String token, LocalDateTime expiryDate, Boolean isRevoked, String deviceInfo, BaseAccount user, LocalDateTime createdAt) {
        this.id = id;
        this.token = token;
        this.expiryDate = expiryDate;
        this.isRevoked = isRevoked;
        this.deviceInfo = deviceInfo;
        this.user = user;
        this.createdAt = createdAt;
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiryDate);
    }
}