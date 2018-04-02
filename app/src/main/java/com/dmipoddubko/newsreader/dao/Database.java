package com.dmipoddubko.newsreader.dao;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.dmipoddubko.newsreader.R;
import com.dmipoddubko.newsreader.api.model.News;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Scanner;

import static com.dmipoddubko.newsreader.utils.Constants.UPDATE_LIST_VIEW;
import static okhttp3.internal.Util.UTF_8;

public class Database extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String NAME = "news_reader";
    private final Context ctx;
    private volatile NewsDAO dao;

    public Database(Context ctx) {
        super(ctx, NAME, null, VERSION);
        this.ctx = ctx;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        scan(R.raw.database_schema_v1, db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.execSQL("PRAGMA foreign_keys=ON;");
    }

    private void scan(int res, SQLiteDatabase db) {
        Scanner s = new Scanner(ctx.getResources().openRawResource(res)).useDelimiter(";");
        String query = null;
        try {
            while (s.hasNext()) {
                query = s.next();
                if (!TextUtils.isEmpty(query)) {
                    db.execSQL(query);
                }
            }
        } catch (SQLiteException e) {
            throw new RuntimeException(String.format("SQLiteException. Query: %s is failed!", query), e);
        } finally {
            s.close();
        }
    }

    public void insertNews(final List<News> news) {
        runTransaction(db -> {
            String sql = read(ctx, R.raw.insert_news);
            SQLiteStatement statement = db().compileStatement(sql);
            for (News n : news) {
                if (isArticleExists(n.getNewsId())) continue;
                statement.clearBindings();
                statement.bindLong(1, n.getNewsId());
                statement.bindString(2, n.getContent());
                statement.bindString(3, n.getTitle());
                statement.executeInsert();
            }
        }, "Can't insert data into table news");
        ctx.sendBroadcast(new Intent(UPDATE_LIST_VIEW));
    }

    private void runTransaction(InTransaction transaction, String msg) {
        SQLiteDatabase db = db();
        try {
            db.beginTransaction();
            transaction.apply(db);
            db().setTransactionSuccessful();
        } catch (Exception e) {
            throw new RuntimeException(msg, e);
        } finally {
            db.endTransaction();
        }
    }

    public boolean isArticleExists(int newsId) {
        return manageCursor(c -> c.getCount() > 0, call(R.raw.is_news_exist, asArguments(newsId),
                String.format("run isArticleExists(String link), link = %s", newsId)));
    }

    @NonNull
    private String[] asArguments(Object... v) {
        String[] args = new String[v.length];
        for (int idx = 0; idx < v.length; idx++)
            args[idx] = String.valueOf(v[idx]);
        return args;
    }

    private interface InTransaction {
        void apply(SQLiteDatabase db);
    }


    public interface CursorFunction<T> {
        T apply(Cursor c);
    }

    public <T> T manageCursor(CursorFunction<T> cursorFunction, Cursor c) {
        try {
            return cursorFunction.apply(c);
        } finally {
            if (c != null) c.close();
        }
    }

    private Cursor call(int id, String[] args, String msg) {
        String sql = read(ctx, id);
        return db().rawQuery(sql, args);
    }

    private SQLiteDatabase db() {
        return getWritableDatabase();
    }

    public NewsDAO dao() {
        if (dao == null)
            dao = new NewsDAO(db());
        return dao;
    }

    private static String read(Context ctx, int id) {
        InputStream is = ctx.getResources().openRawResource(id);
        try {
            return IOUtils.toString(is, UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Unable to read SQL from raw source", e);
        } finally {
            IOUtils.closeQuietly(is);
        }
    }
}