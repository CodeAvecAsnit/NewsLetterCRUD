package com.news.lettercrud.data.model;

import com.news.lettercrud.data.enumeration.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * @author : Asnit Bakhati
 */
@Setter
@Getter
@Entity
@AllArgsConstructor
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

    @Setter
    @Getter
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
    joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(referencedColumnName = "role_id"))
    private List<RoleTable> userRoles;

    public BaseAccount() {
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
