package com.news.lettercrud.Data.model;

import jakarta.persistence.*;

@Entity
public class NewsLetter {
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
    private NewsCategory newsCategory;

}
