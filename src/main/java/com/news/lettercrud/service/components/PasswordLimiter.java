package com.news.lettercrud.service.components;

import java.util.Optional;

public interface PasswordLimiter {
    Optional<Integer> getTries(String email);

    void setTries(String email, int tryNumber);

    void onSuccessRemove(String email, int tryNumber);
}
