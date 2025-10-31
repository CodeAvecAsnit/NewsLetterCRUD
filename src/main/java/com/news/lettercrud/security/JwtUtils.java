package com.news.lettercrud.security;

import com.news.lettercrud.data.Enum.TokenStatus;
import com.news.lettercrud.data.model.BaseAccount;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtils {

    private static final Logger log = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.auth-expiration}")
    private long jwtExpiration;

    @Value("${app.jwt.refresh-threshold}")
    private long refreshThresholdMs;

    private SecretKey secretKey;

    @PostConstruct
    public void init(){
        secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    public String getJwtFromHeader(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        if(bearerToken!=null && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }
        else return null;
    }

    public String generateJwtTokens(UserDetailsImpl userDetailsImpl){
        String username = userDetailsImpl.getUsername();
        String detailsRole = userDetailsImpl.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse("USER_ROLE");

        return Jwts.builder()
                .subject(username)
                .claim("role", detailsRole)
                .claim("id", userDetailsImpl.getId())
                .issuedAt(new Date())
                .expiration(new Date(new Date().getTime() + jwtExpiration))
                .signWith(secretKey)
                .compact();
    }

    public String generateAccessTokenFromUser(BaseAccount user){
        return Jwts.builder()
                .subject(user.getEmail())
                .claim("role", user.getRole())
                .claim("id", user.getId())
                .issuedAt(new Date())
                .expiration(new Date(new Date().getTime() + jwtExpiration))
                .signWith(secretKey)
                .compact();
    }

    public String getUserRoleFromToken(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role", String.class);
    }

    public Long getUserIdFromToken(String token){
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("id", Long.class);
    }

    public String getUserNameFromToken(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean validateJwtToken(String authToken){
        try{
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(authToken);
            return true;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

    public TokenStatus checkTokenStatus(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            Date expiryDate = claims.getExpiration();
            long timeUntilExpiry = expiryDate.getTime() - System.currentTimeMillis();

            if (timeUntilExpiry <= 0) {
                return TokenStatus.EXPIRED;
            } else if (timeUntilExpiry < refreshThresholdMs) {
                return TokenStatus.EXPIRING_SOON;
            } else {
                return TokenStatus.VALID;
            }
        } catch (ExpiredJwtException e) {
            return TokenStatus.EXPIRED;
        } catch (SignatureException | MalformedJwtException |
                 UnsupportedJwtException | IllegalArgumentException e) {
            return TokenStatus.INVALID;
        }
    }

    public long getExpirationTime(){
        return this.jwtExpiration;
    }
}