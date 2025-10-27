package com.news.lettercrud.Services.news;

import com.news.lettercrud.Data.DTOs.CreateORUpdateNewsDTO;
import com.news.lettercrud.Data.model.NewsLetter;
import org.springframework.security.access.prepost.PreAuthorize;

public interface UpdateNewsService {
    NewsLetter updateNews(int newsId, CreateORUpdateNewsDTO newData, long userId);

    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    void updateNewsByAdmin(int newsId, CreateORUpdateNewsDTO newData);
}
