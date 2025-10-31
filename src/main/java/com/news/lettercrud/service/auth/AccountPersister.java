package com.news.lettercrud.service.auth;


import com.news.lettercrud.data.model.BaseAccount;

public interface AccountPersister {
    BaseAccount persist(BaseAccount account);
}