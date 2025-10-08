package com.news.lettercrud.Services.model.impl;

import com.news.lettercrud.Data.model.NewsLetter;
import com.news.lettercrud.Repositories.NewsRepository;
import com.news.lettercrud.Services.model.NewsService;
import com.news.lettercrud.exceptions.NewsNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;

    @Autowired
    public NewsServiceImpl(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    @Override
    public NewsLetter findById(int id){
        return newsRepository.findById(id).
                orElseThrow(()->new NewsNotFoundException("Sorry,news with that id doesn't exist"));
    }

    @Override
    public List<NewsLetter> getTodayNews(LocalDateTime from,LocalDateTime to){
        return newsRepository.getAllByCreatedDateBetween(from,to);
    }

    @Override
    @Transactional
    public void deleteNews(int id){
        newsRepository.deleteById(id);
    }

    @Override
    public void deletenews(NewsLetter newsLetter) {
        newsRepository.delete(newsLetter);
    }


    @Override
    @Transactional
    public void postNews(NewsLetter newsLetter){
        newsRepository.save(newsLetter);
    }

}
