package com.news.lettercrud.Services.auth;


import com.news.lettercrud.Data.model.BaseAccount;

public interface AccountPersister {
    void persist(BaseAccount account);
}