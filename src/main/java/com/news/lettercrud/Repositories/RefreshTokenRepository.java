package com.news.lettercrud.Repositories;

import com.news.lettercrud.Data.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


/**
 * @author : Asnit Bakhait
 */
@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
 Optional<RefreshToken> findByToken(String token);
 List<RefreshToken> findByUserId(Long userId);
 List<RefreshToken> findByUserIdAndIsRevokedFalse(Long userId);
 void deleteByExpiryDateBefore(LocalDateTime date);
}