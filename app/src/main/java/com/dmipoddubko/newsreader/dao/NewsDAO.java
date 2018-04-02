package com.dmipoddubko.newsreader.dao;

import android.database.sqlite.SQLiteDatabase;

import com.dmipoddubko.newsreader.dao.api.INewsDAO;
import com.dmipoddubko.newsreader.dao.models.NewsModel;

public class NewsDAO extends AbstractDAO<NewsModel> implements INewsDAO {

    NewsDAO(SQLiteDatabase db) {
        super(TABLE, db, ID);
    }

    @Override

    protected NewsModel newInstance() {
        return new NewsModel();
    }

    @Override
    protected void toEntity(AbstractDAO.ContentReader r, NewsModel am) {
        super.toEntity(r, am);

        am.newsId = r.getInt(NEWS_ID);
        am.title = r.getString(TITLE);
        am.createdAt = r.getString(CREATED_AT);
        am.content = r.getString(CONTENT);
    }

    @Override
    protected void toContentValues(NewsModel am, AbstractDAO.ContentWriter cw) {
        super.toContentValues(am, cw);
        cw.write(NEWS_ID,am.newsId);
        cw.write(TITLE,am.title);
        cw.write(CREATED_AT,am.createdAt);
        cw.write(CONTENT,am.content);
    }
}
