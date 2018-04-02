package com.dmipoddubko.newsreader.api.provider.local;

import com.dmipoddubko.newsreader.api.model.NewsList;
import com.dmipoddubko.newsreader.api.provider.callback.Callback;

public interface INewsProvider {
    void news(Callback<NewsList> callback);
}