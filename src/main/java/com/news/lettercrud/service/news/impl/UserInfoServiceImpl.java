package com.news.lettercrud.service.news.impl;

import com.news.lettercrud.data.model.BaseAccount;
import com.news.lettercrud.data.model.NewsLetter;
import com.news.lettercrud.service.model.UserService;
import com.news.lettercrud.service.news.UserInfoService;
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
