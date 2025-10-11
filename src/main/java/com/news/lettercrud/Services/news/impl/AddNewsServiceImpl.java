package com.news.lettercrud.Services.news.impl;

import com.news.lettercrud.Data.DTOs.CreateORUpdateNewsDTO;
import com.news.lettercrud.Data.model.BaseAccount;
import com.news.lettercrud.Data.model.NewsCategory;
import com.news.lettercrud.Data.model.NewsLetter;
import com.news.lettercrud.Services.model.CategoryService;
import com.news.lettercrud.Services.model.NewsService;
import com.news.lettercrud.Services.model.UserService;
import com.news.lettercrud.Services.news.AddNewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class AddNewsServiceImpl implements AddNewsService {

    private final UserService userService;
    private final NewsService newsService;
    private final CategoryService categoryService;

    @Autowired
    public AddNewsServiceImpl(@Qualifier("userServiceImpl") UserService userService
                                ,@Qualifier("newsServiceImpl") NewsService newsService,
                          @Qualifier("categoryServiceImpl") CategoryService categoryService) {
        this.userService = userService;
        this.newsService = newsService;
        this.categoryService = categoryService;
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN','COMPANY','SUPER_ADMIN')")
    public boolean postNews(CreateORUpdateNewsDTO data, long userId) {
        BaseAccount author = userService.findById(userId);
        NewsCategory category = categoryService.findByCategoryNameOrCreate(data.getNewsCategory());
        NewsLetter news = CreateORUpdateNewsDTO.buildNewsLetter(data, author, category);
        newsService.postNews(news);
        return true;
    }
}
