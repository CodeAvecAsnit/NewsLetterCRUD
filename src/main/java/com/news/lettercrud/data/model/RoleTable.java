package com.news.lettercrud.data.model;

import jakarta.persistence.*;
import com.news.lettercrud.data.enumeration.Role;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class RoleTable {
    @Id
    @Column(name = "role_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int roleId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToMany(mappedBy = "userRoles")
    private List<BaseAccount> users;
}
