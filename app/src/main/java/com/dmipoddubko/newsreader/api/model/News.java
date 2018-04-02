package com.dmipoddubko.newsreader.api.model;

import com.google.gson.annotations.SerializedName;

public class News {

    @SerializedName("news_id")
    private int newsId;
    private String title;
    private String content;

    public int getNewsId() {
        return newsId;
    }

    public void setNewsId(int newsId) {
        this.newsId = newsId;
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

    @Override
    public String toString() {
        return title;
    }
}
