package com.news.lettercrud.service.news;

import com.news.lettercrud.data.dto.NewsDisplayDTO;

import java.util.List;

public interface BasicNewsService {

    List<NewsDisplayDTO> getTodayNews();

    NewsDisplayDTO getNewsById(int id);

    void deleteNewsByUser(long userId, int newsId);

    void deleteByADMIN(int newsId);
}
