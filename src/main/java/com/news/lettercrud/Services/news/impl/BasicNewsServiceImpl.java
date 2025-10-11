package com.news.lettercrud.Services.news.impl;

import com.news.lettercrud.Data.DTOs.NewsDisplayDTO;
import com.news.lettercrud.Data.model.NewsLetter;
import com.news.lettercrud.Services.model.NewsService;
import com.news.lettercrud.Services.news.BasicNewsService;
import com.news.lettercrud.exceptions.custom.WrongAuthorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BasicNewsServiceImpl implements BasicNewsService {
    private final NewsService newsService;

    @Autowired
    public BasicNewsServiceImpl(NewsService newsService) {
        this.newsService = newsService;
    }

    @Override
    public List<NewsDisplayDTO> getTodayNews(){
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime yesterday = today.minusDays(1);
        return newsService.getTodayNews(yesterday,today).
                stream().map(NewsDisplayDTO::build).toList();
    }

    @Override
    public NewsDisplayDTO getNewsById(int id){
       return NewsDisplayDTO.build(newsService.findById(id));
    }



    @Override
    public void deleteNewsByUser(long userId, int newsId){
        NewsLetter newsLetter = newsService.findById(newsId);
        if(newsLetter.getAuthor().getUserId()!=userId){
            throw new WrongAuthorException();
        }
        newsService.deletenews(newsLetter);
    }

    @Override
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public void deleteByADMIN(int newsId){
        newsService.deleteNews(newsId);
    }

}
