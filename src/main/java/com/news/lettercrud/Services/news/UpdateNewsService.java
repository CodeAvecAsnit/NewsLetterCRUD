package com.news.lettercrud.Services.news;

import com.news.lettercrud.Data.DTOs.CreateORUpdateNewsDTO;
import org.springframework.security.access.prepost.PreAuthorize;

public interface UpdateNewsService {
    void updateNews(int newsId, CreateORUpdateNewsDTO newData, long userId);

    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    void updateNewsByAdmin(int newsId, CreateORUpdateNewsDTO newData);
}
