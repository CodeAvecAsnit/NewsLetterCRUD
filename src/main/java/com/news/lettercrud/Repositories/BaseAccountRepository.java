package com.news.lettercrud.Repositories;

import com.news.lettercrud.Data.model.BaseAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BaseAccountRepository extends JpaRepository<BaseAccount,Long> {
    public BaseAccount findByEmail(String email);
}