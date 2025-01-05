package com.example.campus_news.StringEntity;

import com.mysql.fabric.xmlrpc.base.Data;

import java.sql.Date;

public class ProclamationData {
    private String title, content;
    private int id;
    private Date date;

    public ProclamationData() {
    }

    public ProclamationData(int id, String title, String content, Date date) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
