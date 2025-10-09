package com.news.lettercrud.Controller;

import com.news.lettercrud.Data.DTOs.LoginDTO;
import com.news.lettercrud.Data.DTOs.LoginResponseDT0;
import com.news.lettercrud.Services.auth.LoginService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class LoginController {

    private final LoginService loginService;

    @Autowired
    public LoginController(@Qualifier("loginServiceImpl") LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/sign_in")
    public ResponseEntity<LoginResponseDT0> authenticateUser(@Valid @RequestBody LoginDTO loginUser){
        System.out.println("SIGN IN endpoint hit");
        return ResponseEntity.ok(loginService.login(loginUser));
    }


}
