package com.example.campus_news.StringEntity;

import android.provider.ContactsContract;

import java.sql.Date;


public class CommentInfo {
    private String phone, content, newsId, name;
    private Date date;

    public CommentInfo() {
    }

    public CommentInfo(String phone, String content, Date date, String newsId) {
        this.phone = phone;
        this.content = content;
        this.date = date;
        this.newsId = newsId;
    }

    public String getNewsId() {
        return newsId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
