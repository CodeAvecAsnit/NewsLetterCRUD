package com.news.lettercrud.Data.model;

import com.news.lettercrud.Data.Enum.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "base_accounts")
public class BaseAccount extends AuditTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Email
    @NotBlank
    @Column(nullable = false, unique = false , length = 50)
    private String email;

    @NotBlank
    @Column(nullable = false, length = 60)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;


    public Long getUserId() {
        return userId;
    }


    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "BaseAccount{" +
                "userId=" + userId +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                '}';
    }
}
