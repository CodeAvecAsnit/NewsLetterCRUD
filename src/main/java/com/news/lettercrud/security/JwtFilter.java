package com.news.lettercrud.security;

import com.news.lettercrud.data.Enum.TokenStatus;
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
import java.io.IOException;
import java.util.List;

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

                switch (tokenStatus) {
                    case VALID:
                        authenticateUser(jwt, request);
                        logger.debug("User authenticated successfully");
                        break;

                    case EXPIRING_SOON:
                        authenticateUser(jwt, request);
                        response.setHeader("X-Token-Expiring-Soon", "true");
                        logger.info("Token expiring soon for request: {}", request.getRequestURI());
                        break;

                    case EXPIRED:
                        logger.warn("Token expired for request: {}", request.getRequestURI());
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                                "Access token expired. Please refresh your token.");
                        return;

                    case INVALID:
                        logger.warn("Invalid JWT token detected for request: {}", request.getRequestURI());
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                                "Invalid token. Please login again.");
                        return;
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
            String role = jwtUtils.getUserRoleFromToken(jwt);
            long id = jwtUtils.getUserIdFromToken(jwt);

            List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));
            UserDetailsImpl userDetails = new UserDetailsImpl(id, username, "", authorities);

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            logger.debug("Security context set for user: {} with role: {}", username, role);
        } catch (Exception e) {
            logger.error("Failed to authenticate user from JWT: {}", e.getMessage());
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
     *  Override this to exclude certain paths from JWT filtering
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return path.startsWith("/api/auth/") ||
                path.startsWith("/public/") ||
                path.equals("/health") ||
                path.equals("/error")||
                path.equals(("/api/v1/sign-in"));
    }
}