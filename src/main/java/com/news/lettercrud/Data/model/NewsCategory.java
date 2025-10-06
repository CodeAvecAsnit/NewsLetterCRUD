package com.news.lettercrud.Data.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class NewsCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int categoryId;

    private String categoryName;

    @OneToMany(mappedBy = "newsCategory")
    private List<NewsLetter> newsLetterList;

    public NewsCategory() {
    }

    public NewsCategory(int categoryId, String categoryName, List<NewsLetter> newsLetterList) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.newsLetterList = newsLetterList;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<NewsLetter> getNewsLetterList() {
        return newsLetterList;
    }

    public void setNewsLetterList(List<NewsLetter> newsLetterList) {
        this.newsLetterList = newsLetterList;
    }
}
