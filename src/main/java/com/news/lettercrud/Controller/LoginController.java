package com.news.lettercrud.Controller;
import com.news.lettercrud.Data.DTOs.APIResponseDTO;
import com.news.lettercrud.Data.model.CompanyAccount;
import com.news.lettercrud.Data.model.UserAccount;
import com.news.lettercrud.Services.VerificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Login Controller", description = "Handles User and Company registration and verification")
public class LoginController {

    @Autowired
    private VerificationService verificationService;

    @Autowired
    private AuthService authService;

    @Operation(summary = "Authenticate a new user using Email and password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password Matches. JWT authorized"),
            @ApiResponse(responseCode = "403", description = "Password or Email Error. No JWT"),
            @ApiResponse(responseCode = "500", description = "Login Failed. No JWT")})


    @PostMapping("/sign_in")
    public ResponseEntity<LoginResponseDTO> authenticateUser(@Valid @RequestBody LoginDTO loginUser){
        System.out.println("SIGN IN endpoint hit");
        return ResponseEntity.ok(authService.login(loginUser));
    }


    //function only for testing authority
    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ADMIN_ROLE')")
    public String adminStuff() {
        return "Only for admins!";
    }

    @Operation(summary = "Register a new user and send verification email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Verification email sent successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })

    @PostMapping("signup/user")
    public ResponseEntity<APIResponseDTO> registerUser(@Valid @RequestBody UserAccount userAccount) {
        verificationService.sendMail(userAccount);
        return ResponseEntity.ok(new APIResponseDTO("Verification email sent", null));
    }



    @Operation(summary = "Register a new company and send verification email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Verification email sent successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })

    @PostMapping("signup/company")
    public ResponseEntity<APIResponseDTO> registerCompany(@Valid @RequestBody CompanyAccount companyAccount) {
        verificationService.sendMail(companyAccount);
        return ResponseEntity.ok(new APIResponseDTO("Verification email sent", null));
    }



    @Operation(summary = "Verify the signup email and create account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully registered and signed in"),
            @ApiResponse(responseCode = "400", description = "Invalid verification code"),
            @ApiResponse(responseCode = "401", description = "Code expired"),
            @ApiResponse(responseCode = "404", description = "Account details not found")
    })

    @PostMapping("signup/verify")
    public ResponseEntity<APIResponseDTO> verifySignup(@Valid @RequestBody MailVerificationDTO dto) {
        int code = verificationService.verify(dto);

        if (code == 5) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new APIResponseDTO("Code expired", null));
        }
        if (code == 6) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new APIResponseDTO("Account details not found", null));
        }
        if (code == 1) {
            String jwt = verificationService.generateJwt(dto.getEmail());
            return ResponseEntity.ok(new APIResponseDTO("Successfully Registered", jwt));
        }
        if (code == 0) {
            return ResponseEntity.badRequest()
                    .body(new APIResponseDTO("Invalid verification code", null));
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new APIResponseDTO("Unexpected error", null));
    }



    //function only for testing authority
    @GetMapping("/useronly")
    @PreAuthorize("hasAuthority('USER_ROLE')")
    public String onlyUsers(){
        return "this is only for users";
    }


    //function only for testing endpoints
    @GetMapping("/signup/test")
    public ResponseEntity<APIResponseDTO> test(){
        return ResponseEntity.ok(new APIResponseDTO("Success","NoJwt"));
    }
}