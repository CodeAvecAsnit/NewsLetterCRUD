package com.news.lettercrud.repository;

import com.news.lettercrud.data.model.NewsCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NewsCategoryRepository extends JpaRepository<NewsCategory, Integer> {
    Optional<NewsCategory> findByCategoryName(String categoryName);
}
