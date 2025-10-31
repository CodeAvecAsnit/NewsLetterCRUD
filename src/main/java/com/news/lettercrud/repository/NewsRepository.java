package com.news.lettercrud.repository;

import com.news.lettercrud.data.model.NewsCategory;
import com.news.lettercrud.data.model.NewsLetter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface NewsRepository extends JpaRepository<NewsLetter,Integer> {
    List<NewsLetter> getAllByCreatedDateBetween(LocalDateTime from,LocalDateTime to);
    List<NewsLetter> findByNewsCategory(NewsCategory techCategory);
}
