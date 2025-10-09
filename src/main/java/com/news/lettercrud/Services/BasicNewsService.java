package com.news.lettercrud.Services;

import com.news.lettercrud.Data.model.NewsLetter;
import com.news.lettercrud.Services.model.NewsService;
import com.news.lettercrud.exceptions.WrongAuthorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BasicNewsService {
    private final NewsService newsService;

    @Autowired
    public BasicNewsService(NewsService newsService) {
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

}
