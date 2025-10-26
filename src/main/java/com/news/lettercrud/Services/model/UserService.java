package com.news.lettercrud.Services.model;

import com.news.lettercrud.Data.model.BaseAccount;

public interface UserService {
    BaseAccount findById(long id);
    boolean existsByEmail(String email);
}
