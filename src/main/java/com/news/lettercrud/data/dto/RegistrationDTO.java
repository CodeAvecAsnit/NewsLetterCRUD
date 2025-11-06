package com.news.lettercrud.data.dto;

import com.news.lettercrud.data.enumeration.Role;
import com.news.lettercrud.data.model.UserAccount;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author : Asnit Bakhati
 */
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Schema(description = "Required for User Registration")
public class RegistrationDTO {
    @NotBlank(message = "Username is required")
    private String userName;

    @NotBlank(message = "Email cannot be empty")
    private String email;

    @NotBlank(message="Password is required")
    private String password;


    public static UserAccount buildUser(RegistrationDTO data){
        UserAccount userAccount = new UserAccount();
        userAccount.setUsername(data.getUserName());
        userAccount.setPassword(data.getPassword());
        userAccount.setRealPass(data.getPassword());
        userAccount.setRole(Role.USER);
        userAccount.setEmail(data.getEmail());
        return userAccount;
    }
}
