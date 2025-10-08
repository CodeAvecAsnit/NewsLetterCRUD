package com.news.lettercrud.Services.model.impl;

import com.news.lettercrud.Data.model.NewsCategory;
import com.news.lettercrud.Repositories.NewsCategoryRepository;
import com.news.lettercrud.Services.model.CategoryService;
import com.news.lettercrud.exceptions.ResourceDoesNotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final NewsCategoryRepository newsCategoryRepository;

    @Autowired
    public CategoryServiceImpl(NewsCategoryRepository newsCategoryRepository) {
        this.newsCategoryRepository = newsCategoryRepository;
    }

    @Override
    public NewsCategory findByCategoryName(String category){
        return newsCategoryRepository.findByCategoryName(category).
                orElseThrow(()->new ResourceDoesNotExistException("There is no category"));
    }

    @Override
    public NewsCategory createNewCategory(NewsCategory newsCategory) {
        return newsCategoryRepository.save(newsCategory);
    }
}
