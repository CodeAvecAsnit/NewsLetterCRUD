package com.news.lettercrud.data.DTOs;


import com.news.lettercrud.data.model.BaseAccount;
import com.news.lettercrud.data.model.NewsCategory;
import com.news.lettercrud.data.model.NewsLetter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
@Schema(description = "Fields required to create or update news")
public class CreateORUpdateNewsDTO {
    @NotBlank
    public String headline;

    public String imageURL;
    @NotBlank
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

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setNewsBody(String newsBody) {
        this.newsBody = newsBody;
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
