package com.news.lettercrud.repository;

import com.news.lettercrud.data.model.CompanyAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<CompanyAccount,Long> {

    public boolean existsByEmail(String email);
}
