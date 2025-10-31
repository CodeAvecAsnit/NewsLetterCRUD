package com.news.lettercrud.data.model;

import com.news.lettercrud.data.Enum.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

/**
 * @author : Asnit Bakhati
 */
@Setter
@Getter
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "base_accounts")
public class BaseAccount extends AuditTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

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

    @OneToMany(mappedBy = "user")
    private List<RefreshToken> refreshToken;

    public BaseAccount() {
    }

    public BaseAccount(Long id, String email, String password, String realPass, Role role, Set<NewsLetter> writings, List<RefreshToken> refreshToken) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.realPass = realPass;
        this.role = role;
        this.writings = writings;
        this.refreshToken = refreshToken;
    }


    @Override
    public String toString() {
        return "BaseAccount{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                '}';
    }
}
