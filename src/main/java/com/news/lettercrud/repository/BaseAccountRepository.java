package com.news.lettercrud.repository;

import com.news.lettercrud.data.model.BaseAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BaseAccountRepository extends JpaRepository<BaseAccount,Long> {
    Optional<BaseAccount> findByEmail(String email);
    boolean existsByEmail(String email);
}