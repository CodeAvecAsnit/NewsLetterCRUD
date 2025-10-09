package com.news.lettercrud.Services.news;

import com.news.lettercrud.Data.model.NewsLetter;

import java.util.List;

public interface BasicNewsService {

    List<NewsLetter> getTodayNews();

    NewsLetter getNewsById(int id);

    void deleteNewsByUser(long userId, int newsId);

    void deleteByADMIN(int newsId);
}
