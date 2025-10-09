package com.news.lettercrud.Services;

import com.news.lettercrud.Data.DTOs.CreateORUpdateNewsDTO;
import com.news.lettercrud.Data.model.BaseAccount;
import com.news.lettercrud.Data.model.NewsCategory;
import com.news.lettercrud.Data.model.NewsLetter;
import com.news.lettercrud.Services.model.CategoryService;
import com.news.lettercrud.Services.model.NewsService;
import com.news.lettercrud.Services.model.UserService;
import com.news.lettercrud.exceptions.ResourceDoesNotExistException;
import com.news.lettercrud.exceptions.UnknownException;
import com.news.lettercrud.exceptions.WrongAuthorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.Set;


@Service
public class UserNewsService {

    private final NewsService newsService;
    private final CategoryService categoryService;

    @Autowired
    public UserNewsService(@Qualifier("newsServiceImpl") NewsService newsService
                       ,@Qualifier("categoryServiceImpl")CategoryService categoryService) {
        this.newsService = newsService;
        this.categoryService=categoryService;
    }


    public void updateNews(int newsId,CreateORUpdateNewsDTO newData,long userId){
       NewsLetter oldNews = newsService.findById(newsId);
       if(oldNews.getAuthor().getUserId()!=userId){
           throw new WrongAuthorException();
       }
       updateNews(oldNews,newData);
    }

    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public void updateNewsByAdmin(int newsId,CreateORUpdateNewsDTO newData){
        NewsLetter oldNews = newsService.findById(newsId);
        updateNews(oldNews,newData);
    }

    private void updateNews(NewsLetter oldNews,CreateORUpdateNewsDTO newData){
        NewsCategory category = null;
        try{
            category = categoryService.findByCategoryName(newData.getNewsCategory());
        }catch (ResourceDoesNotExistException ex){
            NewsCategory newsCategory = new NewsCategory();
            newsCategory.setCategoryName(newData.getNewsCategory());
            category = categoryService.createNewCategory(newsCategory);
        }catch (Exception ex){
            throw new UnknownException();
        }finally {
            oldNews.setNewsCategory(category);
            oldNews.setNewsHeadLine(newData.getHeadline());
            oldNews.setImageUrl(newData.getImageURL());
            oldNews.setNewsBody(newData.getNewsBody());
            newsService.postNews(oldNews);
        }
    }

}
