package com.news.lettercrud.Services;

import com.news.lettercrud.Data.model.BaseAccount;
import com.news.lettercrud.Repositories.BaseAccountRepository;
import com.news.lettercrud.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final BaseAccountRepository baseAccountRepository;

    @Autowired
    public UserService(BaseAccountRepository baseAccountRepository) {
        this.baseAccountRepository = baseAccountRepository;
    }


    public BaseAccount findById(long id){
        return baseAccountRepository.findById(id).
                orElseThrow(UserNotFoundException::new);
    }

}
