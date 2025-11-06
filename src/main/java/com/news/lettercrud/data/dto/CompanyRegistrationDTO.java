package com.news.lettercrud.data.dto;

import com.news.lettercrud.data.enumeration.CompanyType;
import com.news.lettercrud.data.enumeration.Role;
import com.news.lettercrud.data.model.CompanyAccount;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Schema(description = "Data required for company Registration")
public class CompanyRegistrationDTO {

    @Email
    @NotBlank(message = "email cannot be empty")
    private String email;

    @NotBlank(message = "Password cannot be empty")
    private String password;

    private String websiteURL;

    @NotBlank(message = "Company name is required for registration")

    private String companyName;

    private String address;
    private String companyType;
    private String logoURL;
    private String companySize;
    private String phoneNo;

    public CompanyRegistrationDTO(String email, String password, String websiteURL, String companyName, String address, String companyType, String logoURL, String companySize, String phoneNo) {
        this.email = email;
        this.password = password;
        this.websiteURL = websiteURL;
        this.companyName = companyName;
        this.address = address;
        this.companyType = companyType;
        this.logoURL = logoURL;
        this.companySize = companySize;
        this.phoneNo = phoneNo;
    }

    public CompanyRegistrationDTO() {
    }


    public static CompanyAccount buildCompany(CompanyRegistrationDTO data){
        CompanyAccount account = new CompanyAccount();
        account.setEmail(data.getEmail());
        account.setPassword(data.getPassword());
        account.setRealPass(data.getPassword());
        account.setRole(Role.COMPANY);
        account.setCompanyName(data.getCompanyName());
        account.setPhoneNumber(data.getPhoneNo());
        account.setAddress(data.getAddress());
        account.setLogoURL(data.getLogoURL());
        account.setCompanySize(data.getCompanySize());
        CompanyType companyType = CompanyType.valueOf(data.getCompanyType());
        account.setCompanyType(companyType);
        return account;
    }
}
