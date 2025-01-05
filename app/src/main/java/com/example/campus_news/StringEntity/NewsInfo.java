package com.example.campus_news.StringEntity;

import java.util.Date;

public class NewsInfo {
    private int id, browse, comments;
    private String title, content, user, label, phone;
    private Date time;
    private String picture, head;

    public NewsInfo() {
    }

    public NewsInfo(int id,String title, String head, String picture, String user, Date time, int browse, int comments, String label) {
        this.id = id;
        this.title = title;
        this.head = head;
        this.picture = picture;
        this.user = user;
        this.time = time;
        this.browse = browse;
        this.comments = comments;
        this.label = label;
    }

    public NewsInfo(String title, String content, String user, String picture) {
        this.title = title;
        this.content = content;
        this.user = user;
        this.picture = picture;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTitle() {
        return title;
    }

    public String getPicture() {
        return picture;
    }

    public String getUser() {
        return user;
    }

    public Date getTime() {
        return time;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public int getBrowse() {
        return browse;
    }

    public void setBrowse(int browse) {
        this.browse = browse;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
