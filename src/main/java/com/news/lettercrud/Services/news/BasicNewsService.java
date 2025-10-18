package com.news.lettercrud.Services.news;

import com.news.lettercrud.Data.DTOs.NewsDisplayDTO;
import com.news.lettercrud.Data.model.NewsLetter;

import java.util.List;

public interface BasicNewsService {

    List<NewsDisplayDTO> getTodayNews();

    NewsDisplayDTO getNewsById(int id);

    void deleteNewsByUser(long userId, int newsId);

    void deleteByADMIN(int newsId);
}
