package com.news.lettercrud.controller;

import com.news.lettercrud.data.dto.APIResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class TestController {

    @Operation(summary = "Check if user has USER_ROLE",
    security =  @SecurityRequirement(name="bearerAuth"))
    @GetMapping("/user-only")
    @PreAuthorize("hasAuthority('USER_ROLE')")
    public String onlyUsers(){
        return "this is only for users";
    }


    @Operation(summary = "Check if user has ADMIN_ROLE",
    security =  @SecurityRequirement(name="bearerAuth"))
    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ADMIN_ROLE')")
    public String adminStuff() {
        return "Only for admins!";
    }

    @Operation(summary = "Test if controller is accessible")
    @GetMapping("/signup/test")
    public ResponseEntity<APIResponseDTO> testConnection(){
        return ResponseEntity.ok(new APIResponseDTO("Success","NoJwt"));
    }

}
