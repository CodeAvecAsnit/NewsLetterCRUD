package com.news.lettercrud.data.DTOs;

import com.news.lettercrud.data.Enum.Role;
import com.news.lettercrud.data.model.UserAccount;
import io.swagger.v3.oas.annotations.media.Schema;
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
    private String userName;
    private String email;
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
