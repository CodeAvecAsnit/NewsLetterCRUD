package com.news.lettercrud.Services.model;

import com.news.lettercrud.Data.model.NewsCategory;

public interface CategoryService {
    NewsCategory findByCategoryName(String category);
    NewsCategory createNewCategory(NewsCategory newsCategory);
}
