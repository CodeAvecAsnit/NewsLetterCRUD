package com.news.lettercrud.repository;

import com.news.lettercrud.data.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount,Long> {
    public boolean existsByEmail(String email);
}