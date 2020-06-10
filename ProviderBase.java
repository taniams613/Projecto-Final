package com.example.a2casopratico;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public abstract class ProviderBase extends ContentProvider {

    protected static final int POST = 1;
    protected static final int POST_ID = 2;
    protected static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private SQLiteDatabase feedsDB;

    protected abstract String getNomeTabela();
    protected Uri feed = null;
    protected abstract Uri getFeed();

    @Override
    public boolean onCreate() {
        Context context = getContext();
        FeedsSQLHelper dbHelper = new FeedsSQLHelper(context);
        feedsDB = dbHelper.getWritableDatabase();
        return (feedsDB != null);
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteQueryBuilder sqlBuilder = new SQLiteQueryBuilder();
        sqlBuilder.setTables(getNomeTabela());
        if (uriMatcher.match(uri) == POST_ID) {
            sqlBuilder.appendWhere(FeedsDB.Posts._ID + " = " + uri.getPathSegments().get(1));
        }
        if (sortOrder == null || sortOrder.equals("")) {
            sortOrder = FeedsDB.Posts.DEFAULT_SORT_ORDER;
        }
        Cursor c = sqlBuilder.query(feedsDB, projection, selection, selectionArgs, null, null, sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        long rowID = feedsDB.replace(getNomeTabela(), "", values);
        if (rowID > 0) {
            Uri _uri = ContentUris.withAppendedId(getFeed(), rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
        throw new SQLException("Failed to insert row into " + uri);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case POST:
                count = feedsDB.delete(getNomeTabela(), selection, selectionArgs);
                break;
            case POST_ID:
                String id = uri.getPathSegments().get(1);
                count = feedsDB.delete(getNomeTabela(),
                        FeedsDB.Posts._ID + " = " + id
                                + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case POST:
                count = feedsDB.update(getNomeTabela(), values, selection, selectionArgs);
                break;
            case POST_ID:
                count = feedsDB.update(getNomeTabela(), values, FeedsDB.Posts._ID + " = " + uri.getPathSegments().get(1)
                        + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
