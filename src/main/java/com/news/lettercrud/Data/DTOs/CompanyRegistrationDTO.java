package com.news.lettercrud.Data.DTOs;

import com.news.lettercrud.Data.Enum.CompanyType;
import com.news.lettercrud.Data.Enum.Role;
import com.news.lettercrud.Data.model.CompanyAccount;

public class CompanyRegistrationDTO {
    private String email;
    private String password;
    private String websiteURL;
    private String companyName;
    private String companyNumber;
    private String address;
    private String companyType;
    private String logoURL;
    private String companySize;
    private String phoneNo;

    public CompanyRegistrationDTO(String email, String password, String websiteURL, String companyName, String companyNumber, String address, String companyType, String logoURL, String companySize, String phoneNo) {
        this.email = email;
        this.password = password;
        this.websiteURL = websiteURL;
        this.companyName = companyName;
        this.companyNumber = companyNumber;
        this.address = address;
        this.companyType = companyType;
        this.logoURL = logoURL;
        this.companySize = companySize;
        this.phoneNo = phoneNo;
    }

    public CompanyRegistrationDTO() {
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
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

    public String getWebsiteURL() {
        return websiteURL;
    }

    public void setWebsiteURL(String websiteURL) {
        this.websiteURL = websiteURL;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyNumber() {
        return companyNumber;
    }

    public void setCompanyNumber(String companyNumber) {
        this.companyNumber = companyNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCompanyType() {
        return companyType;
    }

    public void setCompanyType(String companyType) {
        this.companyType = companyType;
    }

    public String getLogoURL() {
        return logoURL;
    }

    public void setLogoURL(String logoURL) {
        this.logoURL = logoURL;
    }

    public String getCompanySize() {
        return companySize;
    }

    public void setCompanySize(String companySize) {
        this.companySize = companySize;
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
