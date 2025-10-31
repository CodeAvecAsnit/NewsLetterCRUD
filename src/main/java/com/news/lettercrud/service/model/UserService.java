package com.news.lettercrud.service.model;

import com.news.lettercrud.data.model.BaseAccount;

public interface UserService {
    BaseAccount findById(long id);
    boolean existsByEmail(String email);
}
