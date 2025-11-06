package com.news.lettercrud.service.news.impl;

import com.news.lettercrud.data.dto.CreateORUpdateNewsDTO;
import com.news.lettercrud.data.model.BaseAccount;
import com.news.lettercrud.data.model.NewsCategory;
import com.news.lettercrud.data.model.NewsLetter;
import com.news.lettercrud.service.model.CategoryService;
import com.news.lettercrud.service.model.NewsService;
import com.news.lettercrud.service.model.UserService;
import com.news.lettercrud.service.news.AddNewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : Asnit Bakhati
 */
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
    /**
     * Creates a NewsLetter. Cannot be initiated by USER
     */
    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN','COMPANY','SUPER_ADMIN')")
    public NewsLetter postNews(CreateORUpdateNewsDTO data, long userId) {
        BaseAccount author = userService.findById(userId);
        NewsCategory category = categoryService.findByCategoryNameOrCreate(data.getNewsCategory());
        NewsLetter news = CreateORUpdateNewsDTO.buildNewsLetter(data, author, category);
        return newsService.postNews(news);
    }


    @Override
    public List<NewsLetter> findNewsWithCategory(String categoryName) {
        NewsCategory newsCategory = categoryService.findByCategoryName(categoryName);
        return newsService.findByNewsCategory(newsCategory);
    }

}
