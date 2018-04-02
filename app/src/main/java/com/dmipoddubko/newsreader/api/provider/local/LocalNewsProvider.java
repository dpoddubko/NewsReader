package com.dmipoddubko.newsreader.api.provider.local;

import com.dmipoddubko.newsreader.api.model.News;
import com.dmipoddubko.newsreader.api.model.NewsList;
import com.dmipoddubko.newsreader.api.provider.callback.Callback;

import java.util.ArrayList;
import java.util.List;

public class LocalNewsProvider extends AbstractBaseProvider implements INewsProvider {
    private static int id = 1;

    @Override
    public void news(Callback<NewsList> callback) {
        NewsList body = new NewsList();
        List<News> newsList = new ArrayList<>();
        body.setNewsList(newsList);
        int num = 5;
        for (int i = id; i < id + num; i++) {
            News news = new News();
            news.setNewsId(i);
            news.setTitle("News Title " + i);
            news.setContent(i + "News interesting content. News interesting content.");
            newsList.add(news);
        }
        id += num;
        invokeCallback(callback, createSuccess(body));
    }
}
