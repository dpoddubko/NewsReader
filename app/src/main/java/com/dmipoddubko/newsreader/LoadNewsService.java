package com.dmipoddubko.newsreader;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.dmipoddubko.newsreader.api.model.News;
import com.dmipoddubko.newsreader.api.model.NewsList;
import com.dmipoddubko.newsreader.api.provider.callback.FormCallback;
import com.dmipoddubko.newsreader.dao.Database;

import java.util.List;

import retrofit2.Response;

public class LoadNewsService extends Service {

    private Database db;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        db = new Database(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        APIProviderFactory.news(getApplicationContext()).news(new NewsListCallback());
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSelf();
    }

    private class NewsListCallback extends FormCallback<NewsList> {
        @Override
        public void onResponse(Response<NewsList> response) {
            if (response.isSuccessful()) {
                NewsList body = response.body();
                List<News> newsList = body.getNewsList();
                db.insertNews(newsList);
            }
        }

    }
}


