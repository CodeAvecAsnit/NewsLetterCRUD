package com.news.lettercrud.service.news.impl;

import com.news.lettercrud.data.dto.NewsDisplayDTO;
import com.news.lettercrud.data.model.NewsLetter;
import com.news.lettercrud.service.model.NewsService;
import com.news.lettercrud.service.news.BasicNewsService;
import com.news.lettercrud.exception.custom.WrongAuthorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : Asnit Bakhati
 */
@Service
public class BasicNewsServiceImpl implements BasicNewsService {
    private final NewsService newsService;

    @Autowired
    public BasicNewsServiceImpl(NewsService newsService) {
        this.newsService = newsService;
    }

    /**
     * Fetches NewsLetter that were created in last 24 hours
     */
    @Override
    public List<NewsDisplayDTO> getTodayNews(){
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime yesterday = today.minusDays(1);
        return newsService.getTodayNews(yesterday,today).
                stream().map(NewsDisplayDTO::build).toList();
    }

    /**
     * Fetches recently created news
     */
    @Override
    public List<NewsDisplayDTO> getLatest(){
        return newsService.findLatestNews().stream()
                .map(NewsDisplayDTO::build).collect(Collectors.toList());
    }

    /**
     * Fetches the News by NewsId
     */
    @Override
    public NewsDisplayDTO getNewsById(int id){
       return NewsDisplayDTO.build(newsService.findById(id));
    }



    /**
     * Allows the creator of the news to delete the news
     */
    @Override
    public void deleteNewsByUser(long userId, int newsId){
        NewsLetter newsLetter = newsService.findById(newsId);
        if(newsLetter.getAuthor().getId()!=userId){
            throw new WrongAuthorException();
        }
        newsService.deleteNews(newsLetter);
    }

    /**
     * Allows SUPER_ADMIN to delete any news
     */
    @Override
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public void deleteByADMIN(int newsId){
        newsService.deleteNews(newsId);
    }

}
