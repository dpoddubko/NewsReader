package com.dmipoddubko.newsreader.api.provider.local;

import android.os.AsyncTask;


import com.dmipoddubko.newsreader.api.provider.callback.Callback;
import com.google.gson.Gson;


import okhttp3.ResponseBody;
import retrofit2.Response;

import static okhttp3.MediaType.parse;


abstract class AbstractBaseProvider {

    <T> void invokeCallback(final Callback<T> callback, final Response<T> response) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                callback.onResponse(response);
            }
        }.execute();
    }

    protected <T> Response<T> createSuccess(T body) {
        return Response.success(body);
    }

    protected <T> Response<T> createError(T body) {
        return Response.error(400, ResponseBody.create(parse("application/json"),
                new Gson().toJson(body)));
    }
}
