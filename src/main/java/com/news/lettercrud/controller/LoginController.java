package com.news.lettercrud.controller;

import com.news.lettercrud.data.DTOs.LoginDTO;
import com.news.lettercrud.data.DTOs.LoginResponseDT0;
import com.news.lettercrud.service.auth.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : Asnit Bakhati
 */

@RestController
@RequestMapping("/api/v1")
public class LoginController {

    private final LoginService loginService;

    @Autowired
    public LoginController(@Qualifier("loginServiceImpl") LoginService loginService) {
        this.loginService = loginService;
    }

    @Operation(summary = "Authenticate a new user using Email and password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password Matches. JWT authorized"),
            @ApiResponse(responseCode = "403", description = "Password or Email Error. No JWT"),
            @ApiResponse(responseCode = "500", description = "Login Failed. No JWT")})
    @PostMapping("/sign_in")
    public ResponseEntity<LoginResponseDT0> authenticateUser(@Valid @RequestBody LoginDTO loginUser,
                                                             HttpServletResponse response,
                                                             HttpServletRequest request){

        System.out.println("SIGN IN endpoint hit");
        return ResponseEntity.ok(loginService.login(loginUser,response,request));
    }
}
