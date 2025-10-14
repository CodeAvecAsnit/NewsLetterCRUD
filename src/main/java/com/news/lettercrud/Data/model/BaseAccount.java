package com.news.lettercrud.Data.model;

import com.news.lettercrud.Data.Enum.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.Set;

/**
 * @author : Asnit Bakhati
 */
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

    private String realPass;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "author")
    private Set<NewsLetter> writings;

    public BaseAccount() {
    }

    public BaseAccount(Long userId, String email, String password, String realPass, Role role, Set<NewsLetter> writings) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.realPass = realPass;
        this.role = role;
        this.writings = writings;
    }

    public String getRealPass() {
        return realPass;
    }

    public void setRealPass(String realPass) {
        this.realPass = realPass;
    }

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

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Set<NewsLetter> getWritings() {
        return writings;
    }

    public void setWritings(Set<NewsLetter> writings) {
        this.writings = writings;
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
