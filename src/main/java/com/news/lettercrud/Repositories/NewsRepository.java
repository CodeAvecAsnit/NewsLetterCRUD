package com.news.lettercrud.Repositories;

import com.news.lettercrud.Data.model.NewsLetter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface NewsRepository extends JpaRepository<NewsLetter,Integer> {
    List<NewsLetter> getAllByCreatedDateBetween(LocalDateTime from,LocalDateTime to);
}
