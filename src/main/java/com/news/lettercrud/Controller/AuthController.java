package com.news.lettercrud.Controller;
import com.news.lettercrud.Data.DTOs.APIResponseDTO;
import com.news.lettercrud.Data.DTOs.CompanyRegistrationDTO;
import com.news.lettercrud.Data.DTOs.MailVerificationDTO;
import com.news.lettercrud.Data.DTOs.RegistrationDTO;
import com.news.lettercrud.Data.Enum.VerificationResult;
import com.news.lettercrud.Services.auth.impl.AccountRegistrationFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Login Controller", description = "Handles User and Company registration and verification")
public class AuthController {

    private final AccountRegistrationFacade accountRegistrationFacade;

    @Autowired
    public AuthController( AccountRegistrationFacade accountRegistrationFacade){
        this.accountRegistrationFacade = accountRegistrationFacade;
    }


    @Operation(summary = "Register a new user and send verification email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Verification email sent successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })

    @PostMapping("signup/user")
    public ResponseEntity<APIResponseDTO> registerUser(@Valid @RequestBody RegistrationDTO registrationDTO) {
        accountRegistrationFacade.registerUserAccount(registrationDTO);
        return ResponseEntity.ok(new APIResponseDTO("Verification email sent", null));
    }



    @Operation(summary = "Register a new company and send verification email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Verification email sent successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })

    @PostMapping("signup/company")
    public ResponseEntity<APIResponseDTO> registerCompany(@Valid @RequestBody CompanyRegistrationDTO companyAccount) {
        accountRegistrationFacade.registerCompanyAccount(companyAccount);
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

        VerificationResult result =       accountRegistrationFacade.verifyAndCompleteRegistration(dto);
        //TO DO : give jwt as well as the signup response

        // SUCCESS case - code 1
        if (result == VerificationResult.SUCCESS) {
            return ResponseEntity.ok(
                    new APIResponseDTO("Successfully Registered", "dummyJwt")
            );
        }

        // CODE_EXPIRED case - code 5
        if (result == VerificationResult.CODE_EXPIRED) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new APIResponseDTO("Code expired", null));
        }

        // ACCOUNT_NOT_FOUND case - code 6
        if (result == VerificationResult.ACCOUNT_NOT_FOUND) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new APIResponseDTO("Account details not found", null));
        }

        // CODE_MISMATCH case - code 0/2
        if (result == VerificationResult.CODE_MISMATCH) {
            return ResponseEntity.badRequest()
                    .body(new APIResponseDTO("Invalid verification code", null));
        }

        // TOO_MANY_ATTEMPTS case - code 3
        if (result == VerificationResult.TOO_MANY_ATTEMPTS) {
            return ResponseEntity.badRequest()
                    .body(new APIResponseDTO("Too many failed attempts", null));
        }

        // Unexpected error
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new APIResponseDTO("Unexpected error", null));
    }


    @Operation(summary = "Check if email is available")
    @GetMapping("/check-email")
    public ResponseEntity<?> checkEmail(@RequestParam String email) {
        boolean available =
                accountRegistrationFacade.isEmailAvailable(email);
        return ResponseEntity.ok(available);
    }



    @Operation(summary = "Remove the token from HTTP only cookie",
    security =  @SecurityRequirement(name="bearerAuth"))
    @GetMapping("/log-out")
    public String logout(HttpServletResponse response){
        accountRegistrationFacade.expireCookie(response);
        return "Success";
    }
}