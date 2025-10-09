package com.news.lettercrud.Services.news;

import com.news.lettercrud.Data.DTOs.CreateORUpdateNewsDTO;
import org.springframework.security.access.prepost.PreAuthorize;

public interface AddNewsService {
    @PreAuthorize("hasAnyAuthority('ADMIN','COMPANY')")
    boolean postNews(CreateORUpdateNewsDTO data, long userId);
}
