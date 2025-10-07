package com.news.lettercrud.Services;

import com.news.lettercrud.Data.model.BaseAccount;
import com.news.lettercrud.Data.model.NewsLetter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserNewsService {

    private final NewsService newsService;
    private final UserService userService;

    @Autowired
    public UserNewsService(NewsService newsService, UserService userService) {
        this.newsService = newsService;
        this.userService = userService;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','COMPANY')")
    public void postNews(NewsLetter newsLetter,long userId){
        BaseAccount author = userService.findById(userId);
        newsLetter.setAuthor(author);
        newsService.postNews(newsLetter);
    }


    public List<NewsLetter> getTodayNews(){
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime yesterday = today.minusDays(1);
        return newsService.getTodayNews(yesterday,today);
    }

}
