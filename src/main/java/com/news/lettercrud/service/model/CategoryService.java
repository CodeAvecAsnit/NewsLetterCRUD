package com.news.lettercrud.service.model;

import com.news.lettercrud.data.model.NewsCategory;

public interface CategoryService {
    NewsCategory findByCategoryName(String category);
    NewsCategory createNewCategory(NewsCategory newsCategory);
    NewsCategory findByCategoryNameOrCreate(String category);
}
