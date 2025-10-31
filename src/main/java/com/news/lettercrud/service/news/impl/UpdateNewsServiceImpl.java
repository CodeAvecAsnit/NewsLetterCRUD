package com.news.lettercrud.service.news.impl;

import com.news.lettercrud.data.DTOs.CreateORUpdateNewsDTO;
import com.news.lettercrud.data.model.NewsCategory;
import com.news.lettercrud.data.model.NewsLetter;
import com.news.lettercrud.service.model.CategoryService;
import com.news.lettercrud.service.model.NewsService;
import com.news.lettercrud.service.news.UpdateNewsService;
import com.news.lettercrud.exception.custom.WrongAuthorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

/**
 * @author : Asnit Bakhati
 */
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

    /**
     * Allows creator of the NewsLetter to make changes in it.
     */
    @Override
    public NewsLetter updateNews(int newsId,CreateORUpdateNewsDTO newData,long userId){
       NewsLetter oldNews = newsService.findById(newsId);
       if(oldNews.getAuthor().getId()!=userId){
           throw new WrongAuthorException();
       }
       return updateNews(oldNews,newData);
    }

    /**
     * Allows the SUPER_ADMIN to make changes in any NewsLetter.
     */
    @Override
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public void updateNewsByAdmin(int newsId,CreateORUpdateNewsDTO newData){
        NewsLetter oldNews = newsService.findById(newsId);
        updateNews(oldNews,newData);
    }

    /**
     * Private function that handles the updating of NewsLetter
     */
    private NewsLetter updateNews(NewsLetter oldNews,CreateORUpdateNewsDTO newData){
        NewsCategory cat = categoryService.findByCategoryNameOrCreate(newData.getNewsCategory());
        oldNews.setNewsCategory(cat);
        oldNews.setNewsHeadLine(newData.getHeadline());
        oldNews.setImageUrl(newData.getImageURL());
        oldNews.setNewsBody(newData.getNewsBody());
        return newsService.postNews(oldNews);
    }

}
