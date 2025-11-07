package com.news.lettercrud.data.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;


/**
 * @author : Asnit Bakhati
 */
@Entity
@Table(name = "user_accounts")
public class UserAccount extends BaseAccount {

    @Column(nullable = false,unique = true,length = 30)
    private String username;

    public UserAccount(){
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "UserAccount{" +
                "username='" + username + '\'' +
                '}';
    }
}