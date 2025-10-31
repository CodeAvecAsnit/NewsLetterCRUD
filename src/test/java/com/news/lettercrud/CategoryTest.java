package com.news.lettercrud;


import com.news.lettercrud.Data.model.NewsCategory;
import com.news.lettercrud.Repositories.NewsCategoryRepository;
import com.news.lettercrud.Services.model.CategoryService;
import com.news.lettercrud.exceptions.custom.ResourceDoesNotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CategoryTest {

    @Autowired
    private  NewsCategoryRepository newsCategoryRepository;

    @Autowired
    public CategoryTest(NewsCategoryRepository newsCategoryRepository) {
        this.newsCategoryRepository = newsCategoryRepository;
    }


    public NewsCategory findByCategoryName(String category){
        return newsCategoryRepository.findByCategoryName(category).
                orElseThrow(()->new ResourceDoesNotExistException("There is no category"));
    }

    public NewsCategory createNewCategory(NewsCategory newsCategory) {
        return newsCategoryRepository.save(newsCategory);
    }

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
