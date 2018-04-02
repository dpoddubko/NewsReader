package com.dmipoddubko.newsreader.dao.models;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

public class NewsModel implements Serializable{
    public int id;
    public int newsId;
    public String content = StringUtils.EMPTY;
    public String createdAt;
    public String title = StringUtils.EMPTY;

    @Override
    public String toString() {
        return title;
    }
}