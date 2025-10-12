package com.news.lettercrud.Services.news.impl;

import com.news.lettercrud.Data.model.BaseAccount;
import com.news.lettercrud.Data.model.NewsLetter;
import com.news.lettercrud.Services.model.UserService;
import com.news.lettercrud.Services.news.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserInfoServiceImpl implements UserInfoService {

    private final UserService userService;

    @Autowired
    public UserInfoServiceImpl(@Qualifier("userServiceImpl") UserService userService) {
        this.userService = userService;
    }

    /**
     * Using UserId fetches all the NewsLetter created by the USER.
     */
    @Override
    public Set<NewsLetter> getUsersNews(long userId){
        BaseAccount author = userService.findById(userId);
        return author.getWritings();
    }

}
