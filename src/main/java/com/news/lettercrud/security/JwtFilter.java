package com.news.lettercrud.security;

import com.news.lettercrud.data.enumeration.TokenStatus;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
import org.springframework.util.AntPathMatcher;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    @Autowired
    public JwtFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        logger.debug("JwtFilter triggered for request: {}", request.getRequestURI());
        try {
            String jwt = parseJWT(request);

            if (jwt != null) {
                TokenStatus tokenStatus = jwtUtils.checkTokenStatus(jwt);
                logger.debug("Token status: {}", tokenStatus);
                jwtUtils.debugTokenClaims(jwt);

                switch (tokenStatus) {
                    case VALID:
                        authenticateUser(jwt, request);
                        logger.debug("User authenticated successfully");
                        break;

                    case EXPIRING_SOON:
                        authenticateUser(jwt, request);
                        logger.info("Token expiring soon for request: {}", request.getRequestURI());
                        break;

                    case EXPIRED:
                        logger.warn("Token expired for request: {}", request.getRequestURI());
                         break;

                    case INVALID:
                        logger.warn("Invalid JWT token detected for request: {}", request.getRequestURI());
                        break;
                }
            } else {
                logger.debug("No JWT token found in request");
            }
        } catch (Exception e) {
            logger.error("Error processing JWT authentication: {}", e.getMessage(), e);
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Authenticate user from JWT token
     */
    private void authenticateUser(String jwt, HttpServletRequest request) {
        try {
            String username = jwtUtils.getUserNameFromToken(jwt);
            List<String> roles = jwtUtils.getUserRolesFromToken(jwt);
            long id = jwtUtils.getUserIdFromToken(jwt);

            if (username == null || username.isBlank()) {
                logger.warn("JWT token contains invalid username");
                return;
            }

            if (roles == null || roles.isEmpty()) {
                logger.warn("JWT token contains no roles for user: {}", username);
                roles = Collections.emptyList();
            }

            List<GrantedAuthority> authorities = roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            UserDetailsImpl userDetails = new UserDetailsImpl(id, username, "", roles);

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            authorities
                    );

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            logger.debug("Successfully authenticated user: {} with roles: {}", username, roles);

        }  catch (Exception e) {
            logger.error("Failed to authenticate user from JWT: {}", e.getMessage(), e);
        }
    }

    /**
     * Extract JWT token from HTTP request header or cookie
     * @param request HTTP request
     * @return JWT token if found, null otherwise
     */
    private String parseJWT(HttpServletRequest request) {
        String jwt = jwtUtils.getJwtFromHeader(request);
        if (jwt != null) {
            logger.debug("JWT extracted from Authorization header");
            return jwt;
        }
        //fetch from header
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("access_token".equals(cookie.getName())) {
                    logger.debug("JWT extracted from cookie");
                    return cookie.getValue();
                }
            }
        }
        logger.debug("JWT not found in header or cookie");
        return null;
    }

    /**
     *  Override this to the paths from JWT filtering. Can cause a lot of problems
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        AntPathMatcher pathMatcher = new AntPathMatcher();
        return Arrays.stream(SecurityConstants.PUBLIC_URLS)
                .anyMatch(pattern -> pathMatcher.match(pattern, path));
    }
}