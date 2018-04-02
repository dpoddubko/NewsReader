package com.dmipoddubko.newsreader;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.dmipoddubko.newsreader.dao.models.NewsModel;

import static com.dmipoddubko.newsreader.utils.Constants.EXTRAS_NEWS_MODEL;

public class NewsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        NewsModel newsModel = (NewsModel) getIntent().getSerializableExtra(EXTRAS_NEWS_MODEL);
        setTitle(newsModel.title);
        ((TextView)findViewById(R.id.textView)).setText(newsModel.content);
    }
}
