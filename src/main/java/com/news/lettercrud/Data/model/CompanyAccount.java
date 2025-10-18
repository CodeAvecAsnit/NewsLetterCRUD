package com.news.lettercrud.Data.model;

import com.news.lettercrud.Data.Enum.CompanyType;
import com.news.lettercrud.Data.Enum.Role;
import jakarta.persistence.*;


/**
 * @author : Asnit Bakhati
 */
@Entity
@Table(name = "company_accounts")
public class CompanyAccount extends BaseAccount{

    @Column(nullable = false,unique = true)
    private String companyName;

    @Column(nullable = false,unique = true,length = 12)
    private String phoneNumber;

    private String address;

    @Enumerated(EnumType.STRING)
    private CompanyType companyType;

    @Lob
    private String companyWebsite;

    private String logoURL;

    private String companySize;

    public CompanyAccount(){
        this.setRole(Role.COMPANY);
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanySize() {
        return companySize;
    }

    public void setCompanySize(String companySize) {
        this.companySize = companySize;
    }

    public String getLogoURL() {
        return logoURL;
    }

    public void setLogoURL(String logoURL) {
        this.logoURL = logoURL;
    }

    public String getCompanyWebsite() {
        return companyWebsite;
    }

    public void setCompanyWebsite(String companyWebsite) {
        this.companyWebsite = companyWebsite;
    }

    public CompanyType getCompanyType() {
        return companyType;
    }

    public void setCompanyType(CompanyType companyType) {
        this.companyType = companyType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "CompanyAccount{" +
                "companyName='" + companyName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", address='" + address + '\'' +
                ", companyType=" + companyType +
                ", companyWebsite='" + companyWebsite + '\'' +
                ", logoURL='" + logoURL + '\'' +
                ", companySize='" + companySize + '\'' +
                '}';
    }
}