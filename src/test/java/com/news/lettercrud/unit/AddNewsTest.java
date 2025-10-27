package com.news.lettercrud.unit;

import com.news.lettercrud.Data.DTOs.CreateORUpdateNewsDTO;
import com.news.lettercrud.Data.Enum.Role;
import com.news.lettercrud.Data.model.BaseAccount;
import com.news.lettercrud.Data.model.NewsCategory;
import com.news.lettercrud.Data.model.NewsLetter;
import com.news.lettercrud.Repositories.BaseAccountRepository;
import com.news.lettercrud.Repositories.NewsCategoryRepository;
import com.news.lettercrud.Repositories.NewsRepository;
import com.news.lettercrud.Services.model.impl.CategoryServiceImpl;
import com.news.lettercrud.Services.model.impl.NewsServiceImpl;
import com.news.lettercrud.Services.model.impl.UserServiceImpl;
import com.news.lettercrud.Services.news.impl.AddNewsServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;



public class AddNewsTest {

    @Mock
    private BaseAccountRepository baseAccountRepository;

    @Mock
    private NewsCategoryRepository newsCategoryRepository;

    @Mock
    private NewsRepository newsRepository;

    @Mock
    private UserServiceImpl userService;

    @Mock
    private NewsServiceImpl newsService;

    @Mock
    private CategoryServiceImpl categoryService;

    @InjectMocks
    private AddNewsServiceImpl addNewsService;

    public AddNewsTest(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testInsertion(){
        //Create data
        long userId = 1L;

        CreateORUpdateNewsDTO data = new CreateORUpdateNewsDTO();
        data.headline = "Creation of News";
        data.imageURL = "www.google.com/images/news";
        data.newsBody ="A group has successfully written and tested a News Application";
        data.newsCategory = "Recent";

        //Create fake data
        Set<NewsLetter> authorWritings = new HashSet<>();
        BaseAccount fakeUser = new BaseAccount();
        fakeUser.setId(userId);
        fakeUser.setEmail("admin@gmail.com");
        fakeUser.setPassword("RandomPass");
        fakeUser.setWritings(authorWritings);
        fakeUser.setRole(Role.ADMIN);

        //fake news data
        NewsCategory category = new NewsCategory();
        category.setCategoryId(11);
        category.setCategoryName(data.getNewsCategory());

        NewsLetter news = CreateORUpdateNewsDTO.buildNewsLetter(data,fakeUser,category);

        when(userService.findById(1L)).thenReturn(fakeUser);
        when(categoryService.findByCategoryNameOrCreate(data.getNewsCategory())).thenReturn(category);
        when(newsService.postNews(news)).thenReturn(news);
        assertNull(addNewsService.postNews(data,1L));
    }
}
