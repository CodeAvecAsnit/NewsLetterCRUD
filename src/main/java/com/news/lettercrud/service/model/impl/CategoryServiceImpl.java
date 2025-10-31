package com.news.lettercrud.service.model.impl;

import com.news.lettercrud.data.model.NewsCategory;
import com.news.lettercrud.repository.NewsCategoryRepository;
import com.news.lettercrud.service.model.CategoryService;
import com.news.lettercrud.exception.custom.ResourceDoesNotExistException;
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
