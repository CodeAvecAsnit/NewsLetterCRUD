package com.news.lettercrud.Data.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class NewsCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int categoryId;

    private String categoryName;

    @OneToMany
    private List<NewsLetter> newsLetterList;
}
