package com.news.lettercrud.Services.news;

import com.news.lettercrud.Data.DTOs.CreateORUpdateNewsDTO;
import com.news.lettercrud.Data.model.NewsLetter;
import org.springframework.security.access.prepost.PreAuthorize;

public interface AddNewsService {
    @PreAuthorize("hasAnyAuthority('ADMIN','COMPANY')")
    NewsLetter postNews(CreateORUpdateNewsDTO data, long userId);
}
