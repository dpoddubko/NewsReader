package com.dmipoddubko.newsreader.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dmipoddubko.newsreader.dao.api.IDAO;
import com.dmipoddubko.newsreader.dao.api.INewsDAO;

import com.dmipoddubko.newsreader.dao.models.NewsModel;
import com.dmipoddubko.newsreader.utils.AppTimeUtils;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDAO<E extends NewsModel> implements IDAO<E>, INewsDAO {

    private final String table;
    private final SQLiteDatabase db;
    private final String id;

    AbstractDAO(String table, SQLiteDatabase db, String id) {
        this.table = table;
        this.db = db;
        this.id = id;
    }

    protected abstract E newInstance();

    protected void toEntity(ContentReader r, E e) {
        e.id = r.getInt(this.id);
    }

    protected void toContentValues(E e, ContentWriter cw) {
        cw.write(this.id, e.id);
    }

    public int insert(E e) {
        ContentValues cv = toContentValues(e);
        cv.remove(id);
        e.id = (int) db.insertOrThrow(table, null, cv);
        return e.id;
    }

    public int update(E e) {
        return db.update(table, toContentValues(e), this.id + " = ?", asArgument(e.id));
    }

    public int delete(int id) {
        return db.delete(table, this.id + " = ?", asArgument(id));
    }

    public int delete(E e) {
        return db.delete(table, this.id + " = ?", asArgument(e.id));
    }

    public int deleteAll() {
        return db.delete(table, null, null);
    }

    public E findById(int id) {
        String sql = "select * from " + table + " where " + this.id + " = ?";
        Cursor c = db.rawQuery(sql, asArgument(id));
        try {
            if (c.moveToFirst()) {
                E e = newInstance();
                toEntity(ContentReader.create(c), e);
                return e;
            } else {
                return null;
            }
        } finally {
            c.close();
        }
    }

    public List<E> findAll() {
        String sql = "select * from " + table;
        Cursor c = db.rawQuery(sql, null);
        try {
            List<E> result = new ArrayList<>(c.getCount());
            ContentReader cr = ContentReader.create(c);
            while (c.moveToNext()) {
                E e = newInstance();
                toEntity(cr, e);
                result.add(e);
            }
            return result;
        } finally {
            c.close();
        }
    }

    static class ContentReader {
        private final Cursor c;

        ContentReader(Cursor c) {
            this.c = c;
        }

        int getInt(String columnName) {
            return c.getInt(c.getColumnIndexOrThrow(columnName));
        }

        Integer getNullableInt(String columnName) {
            int index = c.getColumnIndexOrThrow(columnName);
            return c.isNull(index) ? null : c.getInt(index);
        }

        String getString(String columnName) {
            return c.getString(c.getColumnIndexOrThrow(columnName));
        }

        DateTime getDateTime(String columnName) {
            return AppTimeUtils.fromString(getString(columnName));
        }

        private static ContentReader create(Cursor c) {
            return new ContentReader(c);
        }
    }

    static class ContentWriter {
        private final ContentValues cv;

        ContentWriter(ContentValues cv) {
            this.cv = cv;
        }

        void write(String name, String value) {
            cv.put(name, value);
        }

        void write(String name, Integer value) {
            cv.put(name, value);
        }

        void write(String name, DateTime value) {
            write(name, AppTimeUtils.toString(value));
        }

        static ContentWriter create(ContentValues cv) {
            return new ContentWriter(cv);
        }
    }

    private ContentValues toContentValues(E e) {
        ContentValues cv = new ContentValues();
        toContentValues(e, ContentWriter.create(cv));
        return cv;
    }

    private String[] asArgument(int id) {
        return new String[]{String.valueOf(id)};
    }
}