package com.dmipoddubko.newsreader;

import android.content.Context;

import com.dmipoddubko.newsreader.api.provider.local.INewsProvider;
import com.dmipoddubko.newsreader.api.provider.local.LocalNewsProvider;
import com.dmipoddubko.newsreader.api.provider.remote.RemoteAPIProvider;

public class APIProviderFactory {

    private APIProviderFactory() {
        // hidden
    }

    public static INewsProvider news(Context context) {
        return true ? new LocalNewsProvider() : new RemoteAPIProvider(context);
    }
}
