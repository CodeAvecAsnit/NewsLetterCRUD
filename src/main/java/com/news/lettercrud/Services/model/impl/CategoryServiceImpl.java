package com.news.lettercrud.Services.model.impl;

import com.news.lettercrud.Data.model.NewsCategory;
import com.news.lettercrud.Repositories.NewsCategoryRepository;
import com.news.lettercrud.Services.model.CategoryService;
import com.news.lettercrud.exceptions.custom.ResourceDoesNotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author : Asnit Bakhati
 */
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

    @Override
    public NewsCategory findByCategoryNameOrCreate(String category) {
        return newsCategoryRepository.findByCategoryName(category).
                orElseGet(()->{
                    NewsCategory cat = new NewsCategory();
                    cat.setCategoryName(category);
                    return newsCategoryRepository.save(cat);
                        }
                        );

    }
}
