package com.dmipoddubko.newsreader.api.provider.remote;

import android.content.Context;
import android.support.annotation.NonNull;

import com.dmipoddubko.newsreader.api.model.NewsList;
import com.dmipoddubko.newsreader.NewsReaderApi;
import com.dmipoddubko.newsreader.api.provider.callback.Callback;
import com.dmipoddubko.newsreader.api.provider.local.INewsProvider;

import retrofit2.Call;
import retrofit2.Response;

public class RemoteAPIProvider implements INewsProvider {

    private final Context context;
    private final NewsReaderApi instance;

    public RemoteAPIProvider(Context context) {
        this.instance = NewsReaderApi.Factory.getInstance();
        this.context = context;
    }

    @NonNull
    private <T> retrofit2.Callback<T> delegateCallback(final Callback<T> callback) {
        return new retrofit2.Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                callback.onResponse(response);
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                callback.onFailure(t);
            }
        };
    }

    @Override
    public void news(Callback<NewsList> callback) {
        instance.news().enqueue(delegateCallback(callback));
    }
}
