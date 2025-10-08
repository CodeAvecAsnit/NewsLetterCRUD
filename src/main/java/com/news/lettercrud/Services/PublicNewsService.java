package com.news.lettercrud.Services;

import com.news.lettercrud.Data.model.NewsLetter;
import com.news.lettercrud.Services.model.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PublicNewsService {
    private final NewsService newsService;

    @Autowired
    public PublicNewsService(NewsService newsService) {
        this.newsService = newsService;
    }

    public List<NewsLetter> getTodayNews(){
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime yesterday = today.minusDays(1);
        return newsService.getTodayNews(yesterday,today);
    }

    public NewsLetter getNewsById(int id){
        return newsService.findById(id);
    }

}
