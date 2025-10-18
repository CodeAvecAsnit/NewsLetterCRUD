package com.news.lettercrud.Data.DTOs;

import com.news.lettercrud.Data.Enum.Role;
import com.news.lettercrud.Data.model.UserAccount;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Required for User Registration")
public class RegistrationDTO {
    private String userName;
    private String email;
    private String password;

    public RegistrationDTO() {
    }

    public RegistrationDTO(String userName, String email, String password) {
        this.userName = userName;
        this.email = email;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

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
