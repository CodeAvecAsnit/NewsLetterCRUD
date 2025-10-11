package com.news.lettercrud.Services.news.impl;

import com.news.lettercrud.Data.DTOs.CreateORUpdateNewsDTO;
import com.news.lettercrud.Data.model.NewsCategory;
import com.news.lettercrud.Data.model.NewsLetter;
import com.news.lettercrud.Services.model.CategoryService;
import com.news.lettercrud.Services.model.NewsService;
import com.news.lettercrud.Services.news.UpdateNewsService;
import com.news.lettercrud.exceptions.WrongAuthorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;


@Service
public class UpdateNewsServiceImpl implements UpdateNewsService {

    private final NewsService newsService;
    private final CategoryService categoryService;

    @Autowired
    public UpdateNewsServiceImpl(@Qualifier("newsServiceImpl") NewsService newsService
                       , @Qualifier("categoryServiceImpl")CategoryService categoryService) {
        this.newsService = newsService;
        this.categoryService=categoryService;
    }


    @Override
    public void updateNews(int newsId,CreateORUpdateNewsDTO newData,long userId){
       NewsLetter oldNews = newsService.findById(newsId);
       if(oldNews.getAuthor().getUserId()!=userId){
           throw new WrongAuthorException();
       }
       updateNews(oldNews,newData);
    }

    @Override
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public void updateNewsByAdmin(int newsId,CreateORUpdateNewsDTO newData){
        NewsLetter oldNews = newsService.findById(newsId);
        updateNews(oldNews,newData);
    }

    private void updateNews(NewsLetter oldNews,CreateORUpdateNewsDTO newData){
        NewsCategory cat = categoryService.findByCategoryNameOrCreate(newData.getNewsCategory());
        oldNews.setNewsCategory(cat);
        oldNews.setNewsHeadLine(newData.getHeadline());
        oldNews.setImageUrl(newData.getImageURL());
        oldNews.setNewsBody(newData.getNewsBody());
        newsService.postNews(oldNews);
    }

}
