package com.news.lettercrud.service.news;

import com.news.lettercrud.data.dto.CreateORUpdateNewsDTO;
import com.news.lettercrud.data.model.NewsLetter;
import org.springframework.security.access.prepost.PreAuthorize;

public interface UpdateNewsService {
    NewsLetter updateNews(int newsId, CreateORUpdateNewsDTO newData, long userId);

    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    void updateNewsByAdmin(int newsId, CreateORUpdateNewsDTO newData);
}
