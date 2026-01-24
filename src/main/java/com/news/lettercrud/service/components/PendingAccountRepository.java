package com.news.lettercrud.service.components;

import com.news.lettercrud.data.model.BaseAccount;
import java.util.Optional;

public interface PendingAccountRepository {

    void store(String email, BaseAccount account);
    Optional<BaseAccount> find(String email);
    void remove(String email);

}