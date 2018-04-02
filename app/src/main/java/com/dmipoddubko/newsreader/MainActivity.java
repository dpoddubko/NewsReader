package com.dmipoddubko.newsreader;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.dmipoddubko.newsreader.dao.Database;
import com.dmipoddubko.newsreader.dao.models.NewsModel;

import java.util.List;

import static com.dmipoddubko.newsreader.utils.Constants.EXTRAS_NEWS_MODEL;
import static com.dmipoddubko.newsreader.utils.Constants.UPDATE_LIST_VIEW;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private Database db;
    private DataUpdateReceiver dataUpdateReceiver;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.progressBar);
        listView = findViewById(R.id.news_list);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            NewsModel newsModel = (NewsModel) parent.getItemAtPosition(position);
            Intent i = new Intent(MainActivity.this, NewsActivity.class);
            i.putExtra(EXTRAS_NEWS_MODEL, newsModel);
            startActivity(i);
        });
        db = new Database(MainActivity.this);
        startLoadNewsService();
        findViewById(R.id.button).setOnClickListener(v -> startLoadNewsService());
        findViewById(R.id.button1).setOnClickListener(v -> {
            db.dao().deleteAll();
            refreshNews();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshNews();
        if (dataUpdateReceiver == null) dataUpdateReceiver = new DataUpdateReceiver();
        IntentFilter updateFilter = new IntentFilter(UPDATE_LIST_VIEW);
        registerReceiver(dataUpdateReceiver, updateFilter);
    }

    private void refreshNews() {
        List<NewsModel> models = db.dao().findAll();
        NewsModel[] arr = new NewsModel[models.size()];
        ArrayAdapter<NewsModel> adapter = new ArrayAdapter<>(
                MainActivity.this,
                android.R.layout.simple_list_item_1,
                models.toArray(arr));
        listView.setAdapter(adapter);
        progressBar.setVisibility(View.GONE);
    }

    private void startLoadNewsService() {
        progressBar.setVisibility(View.VISIBLE);
        startService(new Intent(this, LoadNewsService.class));
    }

    private class DataUpdateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(UPDATE_LIST_VIEW)) {
                refreshNews();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (dataUpdateReceiver != null) unregisterReceiver(dataUpdateReceiver);
    }
}