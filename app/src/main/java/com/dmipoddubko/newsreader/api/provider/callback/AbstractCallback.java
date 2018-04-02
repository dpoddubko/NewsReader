package com.dmipoddubko.newsreader.api.provider.callback;

public abstract class AbstractCallback<T> implements Callback<T> {

    public void onFailure(Throwable t) {
        t.printStackTrace();
    }
}
