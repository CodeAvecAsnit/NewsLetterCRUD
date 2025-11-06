package com.news.lettercrud.data.dto;

import com.news.lettercrud.util.DateUtils;
import com.news.lettercrud.data.enumeration.Role;
import com.news.lettercrud.data.model.BaseAccount;
import com.news.lettercrud.data.model.CompanyAccount;
import com.news.lettercrud.data.model.NewsLetter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author : Asnit Bakhati
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Data fields that can be seen by user")
public class NewsDisplayDTO {
    private String headline;
    private String newsDate;
    private String imageURL;
    private String authorName;
    private String body;
    private String category;


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
