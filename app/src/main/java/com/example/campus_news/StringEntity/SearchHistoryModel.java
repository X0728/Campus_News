package com.example.campus_news.StringEntity;

public class SearchHistoryModel {
    String content;
    String key;

    public SearchHistoryModel(String content, String key) {
        this.content = content;
        this.key = key;
    }

    public SearchHistoryModel() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
