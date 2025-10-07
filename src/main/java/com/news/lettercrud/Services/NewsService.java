package com.news.lettercrud.Services;

import com.news.lettercrud.Data.model.NewsLetter;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface NewsService {
    NewsLetter findById(int id);

    List<NewsLetter> getTodayNews(LocalDateTime from, LocalDateTime to);

    @Transactional
    void deleteNews(int id);

    @Transactional
    void postNews(NewsLetter newsLetter);
}
