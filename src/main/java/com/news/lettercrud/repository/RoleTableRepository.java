package com.news.lettercrud.repository;

import com.news.lettercrud.data.model.RoleTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.news.lettercrud.data.enumeration.Role;

@Repository
public interface RoleTableRepository extends JpaRepository<RoleTable,Integer> {
    RoleTable findByRole(Role role);
}
