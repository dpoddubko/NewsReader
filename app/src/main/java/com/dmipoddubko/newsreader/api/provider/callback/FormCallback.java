package com.dmipoddubko.newsreader.api.provider.callback;

import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

import com.dmipoddubko.newsreader.api.model.Error;

import java.util.ArrayList;
import java.util.List;

public abstract class FormCallback<T> extends AbstractCallback<T> implements Callback<T> {

    protected Error getError(List<Error> errors, String key) {
        if (errors == null) return null;

        for (Error error : errors) {
            if (error.getKey().equals(key)) {
                return error;
            }
        }

        return null;
    }

    protected void applyErrorIfExists(TextView e, String key, List<Error> errors) {
        Error error = getError(errors, key);
        if (error != null) {
            e.setError(error.getValue());
            e.requestFocus();
        }
    }

    protected void applyErrorForOther(List<Error> errors, List<String> keys, Context context) {
        List<String> messages = new ArrayList<>();

        for (Error error : errors) {
            if (!keys.contains(error.getKey())) {
                messages.add(error.getValue());
            }
        }

        if (!messages.isEmpty()) {
            Toast.makeText(context, messages.toString(), Toast.LENGTH_LONG).show();
        }
    }
}
