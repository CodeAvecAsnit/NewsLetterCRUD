package com.news.lettercrud.Security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.checkerframework.checker.units.qual.A;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * @author : Asnit Bakhati
 */

@Component
public class JwtFilter  extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;


    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    @Autowired
    public JwtFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        logger.info("JwtFilter triggered for request: " + request.getRequestURI());

        try {
            String jwt = parseJWT(request);

            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                String username = jwtUtils.getUserNameFromToken(jwt);
                String role = jwtUtils.getUserRoleFromToken(jwt);
                long id = jwtUtils.getUserIdFromToken(jwt);
                logger.info("Authenticated user: " + username + " with role: " + role);
                List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));
                UserDetailsImpl userDetailsImpl = new UserDetailsImpl(id,username,"",authorities);
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetailsImpl, null, authorities);

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication : {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }


    /**
     *
     * @param request used for extracting the jwt token from http request or cookie
     * @return jwt token if parsed successfully
     */
    private String parseJWT(HttpServletRequest request){
        String jwt = jwtUtils.getJwtFromHeader(request);
        if (jwt != null) {
            logger.debug("JWT extracted from Authorization header: {}", jwt);
            return jwt;
        }
        Cookie[] cookies = request.getCookies();
        if ( cookies!= null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("access_token")) {
                    logger.debug("JWT extracted from Cookie: {}", cookie.getValue());
                    return cookie.getValue();
                }
            }
        }
        logger.debug("JWT not found in header or cookie");
        return null;
    }
}
