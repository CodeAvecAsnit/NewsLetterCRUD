package com.news.lettercrud.Data.DTOs;

import Additional.DateUtils;
import com.news.lettercrud.Data.Enum.Role;
import com.news.lettercrud.Data.model.BaseAccount;
import com.news.lettercrud.Data.model.CompanyAccount;
import com.news.lettercrud.Data.model.NewsLetter;

public class NewsDisplayDTO {
    private String headline;
    private String newsDate;
    private String imageURL;
    private String authorName;
    private String body;
    private String category;

    public NewsDisplayDTO() {
    }

    public NewsDisplayDTO(String headline, String newsDate, String imageURL, String authorName, String body,String category) {
        this.headline = headline;
        this.newsDate = newsDate;
        this.imageURL = imageURL;
        this.authorName = authorName;
        this.body = body;
        this.category = category;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getNewsDate() {
        return newsDate;
    }

    public void setNewsDate(String newsDate) {
        this.newsDate = newsDate;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public static NewsDisplayDTO build(NewsLetter newsLetter){
        NewsDisplayDTO data = new NewsDisplayDTO();
        BaseAccount account = newsLetter.getAuthor();
        if(account.getRole()== Role.COMPANY){
            CompanyAccount company = (CompanyAccount) account;
            data.setAuthorName(company.getCompanyName());
        }else {
            data.setAuthorName("By Admin");
        }
        data.setBody(newsLetter.getNewsBody());
        data.setCategory(newsLetter.getNewsCategory().getCategoryName());
        data.setHeadline(newsLetter.getNewsHeadLine());
        data.setImageURL(newsLetter.getImageUrl());
        data.setNewsDate(DateUtils.buildDate(newsLetter.getCreatedDate()));
        return data;
    }
}
