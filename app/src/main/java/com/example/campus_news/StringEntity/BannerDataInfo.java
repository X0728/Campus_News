package com.example.campus_news.StringEntity;

public class BannerDataInfo {
    private String image;
    private String title;

    public BannerDataInfo() {
    }

    public BannerDataInfo(String image, String title) {
        this.image = image;
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
