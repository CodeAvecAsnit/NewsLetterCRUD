package com.news.lettercrud.Repositories;

import com.news.lettercrud.Data.model.NewsCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NewsCategoryRepository extends JpaRepository<NewsCategory, Integer> {
    Optional<NewsCategory> findByCategoryName(String categoryName);
}
