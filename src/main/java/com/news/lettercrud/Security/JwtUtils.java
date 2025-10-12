package com.news.lettercrud.Security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.antlr.v4.runtime.misc.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.util.Date;

/**
 * @author : Asnit Bakhati
 */
@Component
public class JwtUtils {

    private static final Logger logger = (Logger) LoggerFactory.getLogger(Utils.class);
    private static final String jwtSecret = "haljldfskjakljfldasjlfjaslfsjaldkfjsaldkfjsaldkfjsaldkfjsaldkfjsaldkfjsaldkfjsaldkfjsaldkfjsaldkfj";
    private static final long jwtExpiration = 86400000;
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
        String detailsRole = userDetailsImpl.getAuthorities().stream().findFirst().map(GrantedAuthority::getAuthority).orElse("USER_ROLE");
        return Jwts.builder().subject(username).claim("role",detailsRole)
                .claim("id",userDetailsImpl.getId()).
                issuedAt(new Date()).
                expiration(new Date(new Date().getTime()+jwtExpiration)).
                signWith(secretKey).
                compact();
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
        return Jwts.parser().
                verifyWith(secretKey).
                build().parseSignedClaims(token).
                getPayload().get("id",Long.class);
    }

    public String getUserNameFromToken(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean validateJwtToken(String authToken){
        try{
            Jwts.parser().verifyWith((SecretKey) secretKey).build().parseSignedClaims(authToken);
            return true;
        }catch (Exception ex){
            logger.error("Problem with the token");
        }
        return false;
    }
}