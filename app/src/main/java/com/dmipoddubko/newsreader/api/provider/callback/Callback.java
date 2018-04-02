package com.dmipoddubko.newsreader.api.provider.callback;

import retrofit2.Response;

public interface Callback<T> {

    void onResponse(Response<T> response);

    void onFailure(Throwable t);
}
