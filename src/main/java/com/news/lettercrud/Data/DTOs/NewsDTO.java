package com.news.lettercrud.Data.DTOs;

public class NewsDTO {
    private String newsHeadline;
    private String authorName;
    private String newsReleaseDate;
    private String newsBody;

    public NewsDTO(String newsHeadline, String authorName, String newsReleaseDate, String newsBody) {
        this.newsHeadline = newsHeadline;
        this.authorName = authorName;
        this.newsReleaseDate = newsReleaseDate;
        this.newsBody = newsBody;
    }

    public NewsDTO() {
    }

    public String getNewsHeadline() {
        return newsHeadline;
    }

    public void setNewsHeadline(String newsHeadline) {
        this.newsHeadline = newsHeadline;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getNewsReleaseDate() {
        return newsReleaseDate;
    }

    public void setNewsReleaseDate(String newsReleaseDate) {
        this.newsReleaseDate = newsReleaseDate;
    }

    public String getNewsBody() {
        return newsBody;
    }

    public void setNewsBody(String newsBody) {
        this.newsBody = newsBody;
    }
}
