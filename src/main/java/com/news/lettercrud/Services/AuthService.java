package com.news.lettercrud.Services;

import com.news.lettercrud.Data.DTOs.LoginDTO;
import com.news.lettercrud.Data.DTOs.LoginResponseDT0;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsImplService userDetailsImplService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    private boolean checkPassword(LoginDTO logUser) {
        UserDetails user = userDetailsImplService.loadUserByUsername(logUser.getEmail());
        return passwordEncoder.matches(logUser.getPassword(), user.getPassword());
    }

    public LoginResponseDT0 logIn(LoginDTO request) {
        //TODO : Implement a Password rate limiting
        try {
            boolean matcher = checkPassword(request);
            if (matcher) {
                String token = jwtUtils.generateJwtTokens(userDetailsImplService.loadUserByUsername(request.getEmail()));
                return new LoginResponseDT0(200, token, "Success");
            } else return new LoginResponseDT0(403, "No token", "Invalid Password or Email");
        }catch (Exception ex) {
            logger.error(ex.getMessage());
            return new LoginResponseDT0(403, "No token", "Invalid Password or Email");
        }
    }


    public LoginResponseDTO login(LoginDTO request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            String token = jwtUtils.generateJwtTokens(userDetailsImplService.loadUserByUsername(request.getEmail()));

            return new LoginResponseDTO(200,token,"Success");

        } catch (BadCredentialsException ex) {
            return new LoginResponseDTO(403,"No token","Password didn't match");
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            return new LoginResponseDTO(500,"No token","Login Failed");
        }
    }
}