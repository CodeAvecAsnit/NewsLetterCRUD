package com.news.lettercrud.service.news;

import com.news.lettercrud.data.model.NewsLetter;

import java.util.Set;

public interface UserInfoService {

    Set<NewsLetter> getUsersNews(long userId);
}
