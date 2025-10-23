package com.news.lettercrud.Security;

import com.news.lettercrud.Data.Enum.TokenStatus;
import com.news.lettercrud.Data.model.BaseAccount;
import com.news.lettercrud.Data.model.RefreshToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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

    @Lazy
    private final RefreshTokenService refreshTokenService;

    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    @Autowired
    public JwtFilter(JwtUtils jwtUtils, RefreshTokenService refreshTokenService) {
        this.jwtUtils = jwtUtils;
        this.refreshTokenService = refreshTokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        logger.info("JwtFilter triggered for request: " + request.getRequestURI());

        try {
            String jwt = parseJWT(request);

            if (jwt != null) {
                TokenStatus tokenStatus = jwtUtils.checkTokenStatus(jwt);
                logger.debug("Token status: {}", tokenStatus);

                switch (tokenStatus) {
                    case VALID:
                        authenticateUser(jwt, request);
                        break;

                    case EXPIRING_SOON:
                    case EXPIRED:

                        logger.info("Token {} - attempting refresh", tokenStatus);
                        String refreshToken = parseRefreshToken(request);

                        if (refreshToken != null && attemptTokenRefresh(refreshToken, response, request)) {
                            logger.info("Token refreshed successfully");
                        } else {
                            logger.warn("Token refresh failed - sending 401");
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                                    "Token expired. Please login again.");
                            return;
                        }
                        break;

                    case INVALID:
                        logger.warn("Invalid JWT token detected");
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                                "Invalid token. Please login again.");
                        return;
                }
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication : {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Attempts to refresh the access token using refresh token
     * @return true if refresh successful, false otherwise
     */
    private boolean attemptTokenRefresh(String refreshToken, HttpServletResponse response,
                                        HttpServletRequest request) {
        try {
            RefreshToken validatedToken = refreshTokenService.validateRefreshToken(refreshToken);
            BaseAccount user = validatedToken.getUser();

            String newAccessToken = jwtUtils.generateAccessTokenFromUser(user);

            response.setHeader("X-New-Access-Token", newAccessToken);

            Cookie jwtCookie = new Cookie("access_token", newAccessToken);
            jwtCookie.setHttpOnly(true);
            jwtCookie.setSecure(false); // Use in production with HTTPS
            jwtCookie.setPath("/");
            jwtCookie.setMaxAge((int) (jwtUtils.getExpirationTime() / 1000));
            response.addCookie(jwtCookie);

            authenticateUserDirectly(user, request);

            logger.info("Token refreshed successfully for user: {}", user.getId());
            return true;

        } catch (Exception e) {
            logger.error("Refresh token validation failed: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Authenticate user from JWT token
     */
    private void authenticateUser(String jwt, HttpServletRequest request) {
        String username = jwtUtils.getUserNameFromToken(jwt);
        String role = jwtUtils.getUserRoleFromToken(jwt);
        long id = jwtUtils.getUserIdFromToken(jwt);
        authenticateUser(role,id,username,request);
    }

    /**
     * Authenticate user directly from User object (after refresh)
     */
    private void authenticateUserDirectly(BaseAccount user, HttpServletRequest request) {
        String role = user.getRole().name();
        Long id = user.getId();
        String username = user.getEmail();
        authenticateUser(role,id,username,request);
    }

    private void authenticateUser(String role,long id,String username,HttpServletRequest request){
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));
        UserDetailsImpl userDetailsImpl = new UserDetailsImpl(id, username, "", authorities);
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetailsImpl, null, authorities);
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    /**
     * Extract JWT token from HTTP request or cookie
     * @param request used for extracting the jwt token
     * @return jwt token if parsed successfully
     */
    private String parseJWT(HttpServletRequest request) {
        String jwt = jwtUtils.getJwtFromHeader(request);
        if (jwt != null) {
            logger.debug("JWT extracted from Authorization header: {}", jwt);
            return jwt;
        }

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
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

    /**
     * Extract refresh token from cookie or header
     * @param request used for extracting the refresh token
     * @return refresh token if found
     */
    private String parseRefreshToken(HttpServletRequest request) {
        String refreshToken = request.getHeader("X-Refresh-Token");
        if (refreshToken != null && !refreshToken.isEmpty()) {
            logger.debug("Refresh token extracted from header");
            return refreshToken;
        }
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("refresh_token")) {
                    logger.debug("Refresh token extracted from cookie: {}", cookie.getValue());
                    return cookie.getValue();
                }
            }
        }
        logger.debug("Refresh token not found in header or cookie");
        return null;
    }
}
