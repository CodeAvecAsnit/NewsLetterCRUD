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
    private final UserService userService;
    private final CategoryService categoryService;

    @Autowired
public UserNewsService(@Qualifier("newsServiceImpl") NewsService newsService,
                       @Qualifier("userServiceImpl") UserService userService
                       ,@Qualifier("categoryServiceImpl")CategoryService categoryService) {
        this.newsService = newsService;
        this.userService = userService;
        this.categoryService=categoryService;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','COMPANY')")
    public void postNews(CreateORUpdateNewsDTO data, long userId){
        BaseAccount author = userService.findById(userId);
        NewsCategory category = categoryService.findByCategoryName(data.getNewsCategory());
        NewsLetter news = CreateORUpdateNewsDTO.buildNewsLetter(data,author,category);
        newsService.postNews(news);
    }

    public Set<NewsLetter> getUsersNews(long userId){
        BaseAccount author = userService.findById(userId);
        return author.getWritings();
    }

    public void deleteNewsByUser(int userId,int newsId){
        NewsLetter newsLetter = newsService.findById(newsId);
        if(newsLetter.getAuthor().getUserId()!=userId){
            throw new WrongAuthorException();
        }
        newsService.deletenews(newsLetter);
    }

    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public void deleteByADMIN(int newsId){
        newsService.deleteNews(newsId);
    }

    public void updateNews(int newsId,CreateORUpdateNewsDTO newData,long userId){
       NewsLetter oldNews = newsService.findById(newsId);
       if(oldNews.getAuthor().getUserId()!=userId){
           throw new WrongAuthorException();
       }
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

    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public void updateNewsByAdmin(int newsId,CreateORUpdateNewsDTO newData){
        NewsLetter oldNews = newsService.findById(newsId);
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
