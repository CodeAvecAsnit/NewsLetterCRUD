package com.news.lettercrud.Data.DTOs;


import com.news.lettercrud.Data.model.BaseAccount;
import com.news.lettercrud.Data.model.NewsCategory;
import com.news.lettercrud.Data.model.NewsLetter;

public class CreateORUpdateNewsDTO {
    public String headline;
    public String imageURL;
    public String newsBody;
    public String newsCategory;

    public CreateORUpdateNewsDTO() {
    }

    public CreateORUpdateNewsDTO(String headline, String imageURL, String newsBody, String newsCategory) {
        this.headline = headline;
        this.imageURL = imageURL;
        this.newsBody = newsBody;
        this.newsCategory = newsCategory;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getNewsBody() {
        return newsBody;
    }

    public void setNewsBody(String newsBody) {
        this.newsBody = newsBody;
    }

    public String getNewsCategory() {
        return newsCategory;
    }

    public void setNewsCategory(String newsCategory) {
        this.newsCategory = newsCategory;
    }

    public static NewsLetter buildNewsLetter(CreateORUpdateNewsDTO data, BaseAccount account, NewsCategory category){
        NewsLetter newsLetter = new NewsLetter();
        newsLetter.setNewsHeadLine(data.getHeadline());
        newsLetter.setImageUrl(data.getImageURL());
        newsLetter.setNewsBody(data.getNewsBody());
        newsLetter.setAuthor(account);
        newsLetter.setNewsCategory(category);
        return newsLetter;
    }

}
