package com.news.lettercrud.data.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

/**
 * @author : Asnit Bakhati
 */
@Entity
public class NewsLetter extends AuditTable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int newsId;

    @Column(columnDefinition = "text")
    private String newsHeadLine;

    @Column
    private String imageUrl;

    @Column(columnDefinition = "text")
    private String newsBody;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @JsonIgnore
    private NewsCategory newsCategory;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private BaseAccount author;


    public NewsLetter(){}

    public NewsLetter(int newsId, String newsHeadLine, String imageUrl, String newsBody, NewsCategory newsCategory, BaseAccount author) {
        this.newsId = newsId;
        this.newsHeadLine = newsHeadLine;
        this.imageUrl = imageUrl;
        this.newsBody = newsBody;
        this.newsCategory = newsCategory;
        this.author = author;
    }

    public int getNewsId() {
        return newsId;
    }

    public void setNewsId(int newsId) {
        this.newsId = newsId;
    }

    public String getNewsHeadLine() {
        return newsHeadLine;
    }

    public void setNewsHeadLine(String newsHeadLine) {
        this.newsHeadLine = newsHeadLine;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getNewsBody() {
        return newsBody;
    }

    public void setNewsBody(String newsBody) {
        this.newsBody = newsBody;
    }

    public NewsCategory getNewsCategory() {
        return newsCategory;
    }

    public void setNewsCategory(NewsCategory newsCategory) {
        this.newsCategory = newsCategory;
    }

    public BaseAccount getAuthor() {
        return author;
    }

    public void setAuthor(BaseAccount author) {
        this.author = author;
    }

}
