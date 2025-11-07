package com.news.lettercrud.service.model;

import com.news.lettercrud.data.model.NewsCategory;
import com.news.lettercrud.data.model.NewsLetter;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface NewsService {

    NewsLetter findById(int id);

    List<NewsLetter> getTodayNews(LocalDateTime from, LocalDateTime to);

    @Transactional
    void deleteNews(int id);

    @Transactional
    void deleteNews(NewsLetter newsLetter);

    @Transactional
    NewsLetter postNews(NewsLetter newsLetter);

    @Transactional
    List<NewsLetter> findByNewsCategory(NewsCategory newsCategory);
}
