package com.news.lettercrud.Services;

import com.news.lettercrud.Data.model.NewsLetter;
import com.news.lettercrud.Repositories.NewsRepository;
import com.news.lettercrud.exceptions.NewsNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NewsServiceImpl {

    private final NewsRepository newsRepository;

    @Autowired
    public NewsServiceImpl(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }


    public NewsLetter findById(int id){
        return newsRepository.findById(id).
                orElseThrow(()->new NewsNotFoundException("Sorry,news with that id doesn't exist"));
    }

    public List<NewsLetter> getTodayNews(LocalDateTime from,LocalDateTime to){
//        LocalDateTime dateTime = LocalDateTime.now();
//        LocalDateTime yesterdayTime = dateTime.minusHours(24);
        return newsRepository.getAllByCreatedDateBetween(from,to);
    }



}
