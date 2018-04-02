package com.dmipoddubko.newsreader;

import com.dmipoddubko.newsreader.api.model.NewsList;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public interface NewsReaderApi {

    String BASE_URL = "https://api.test.com/";

    @GET("news")
    Call<NewsList> news();

    class Factory {
        private static NewsReaderApi service;

        private Factory() {
            // hidden
        }

        public static NewsReaderApi getInstance() {
            if (service == null) {
                service = new Retrofit.Builder()
                        .addConverterFactory(GsonConverterFactory.create())
                        .baseUrl(BASE_URL)
                        .build()
                        .create(NewsReaderApi.class);
            }
            return service;
        }
    }
}
