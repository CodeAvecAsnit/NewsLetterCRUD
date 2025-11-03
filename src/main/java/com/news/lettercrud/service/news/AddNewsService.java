package com.news.lettercrud.service.news;

import com.news.lettercrud.data.DTOs.CreateORUpdateNewsDTO;
import com.news.lettercrud.data.model.NewsLetter;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface AddNewsService {
    @PreAuthorize("hasAnyAuthority('ADMIN','COMPANY')")
    NewsLetter postNews(CreateORUpdateNewsDTO data, long userId);

    List<NewsLetter> findNewsWithCategory(String categoryName);
}
