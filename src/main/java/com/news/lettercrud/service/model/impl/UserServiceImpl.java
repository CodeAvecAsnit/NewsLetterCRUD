package com.news.lettercrud.service.model.impl;

import com.news.lettercrud.data.model.BaseAccount;
import com.news.lettercrud.repository.BaseAccountRepository;
import com.news.lettercrud.service.model.UserService;
import com.news.lettercrud.exception.custom.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author : Asnit Bakhati
 */
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

    public BaseAccount findByEmail(String email){
        return baseAccountRepository.findByEmail(email);
    }

}
