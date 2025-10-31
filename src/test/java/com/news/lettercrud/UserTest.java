package com.news.lettercrud;


import com.news.lettercrud.Data.model.BaseAccount;
import com.news.lettercrud.Repositories.BaseAccountRepository;
import com.news.lettercrud.Services.model.UserService;
import com.news.lettercrud.exceptions.custom.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserTest{


    private BaseAccountRepository baseAccountRepository;

    @Autowired
    public UserTest(BaseAccountRepository baseAccountRepository) {
        this.baseAccountRepository = baseAccountRepository;
    }

    public BaseAccount findById(long id){
        return baseAccountRepository.findById(id).
                orElseThrow(UserNotFoundException::new);
    }

    public boolean existsByEmail(String email){
        return baseAccountRepository.existsByEmail(email);
    }


}
