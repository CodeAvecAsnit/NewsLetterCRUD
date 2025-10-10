package com.news.lettercrud.Services.model.impl;

import com.news.lettercrud.Data.model.BaseAccount;
import com.news.lettercrud.Repositories.BaseAccountRepository;
import com.news.lettercrud.Services.model.UserService;
import com.news.lettercrud.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final BaseAccountRepository baseAccountRepository;

    @Autowired
    public UserServiceImpl(BaseAccountRepository baseAccountRepository) {
        this.baseAccountRepository = baseAccountRepository;
    }

    @Override
    public BaseAccount findById(long id){
        return baseAccountRepository.findById(id).
                orElseThrow(UserNotFoundException::new);
    }

    @Override
    public boolean existsByEmail(String email){
        return baseAccountRepository.existsByEmail(email);
    }


}
