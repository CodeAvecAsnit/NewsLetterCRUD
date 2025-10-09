package com.news.lettercrud.Services.news;

import com.news.lettercrud.Data.model.NewsLetter;

import java.util.Set;

public interface UserInfoService {

    Set<NewsLetter> getUsersNews(long userId);
}
