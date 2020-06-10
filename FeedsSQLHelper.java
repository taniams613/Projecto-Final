package com.example.a2casopratico;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FeedsSQLHelper extends SQLiteOpenHelper {

    public FeedsSQLHelper (Context context) {
        super(context, FeedsDB.DB_NAME, null, FeedsDB.DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        if (db.isReadOnly()) {
            db = getWritableDatabase();
        }

        db.execSQL("CREATE TABLE " + FeedsDB.Posts.TABELA_CORREIO + "("
                + FeedsDB.Posts._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + FeedsDB.Posts.TITLE + " TEXT, "
                + FeedsDB.Posts.LINK + " TEXT UNIQUE, "
                + FeedsDB.Posts.GUID + " TEXT, "
                + FeedsDB.Posts.COMMENTS + " TEXT, "
                + FeedsDB.Posts.PUB_DATE + " INTEGER, "
                + FeedsDB.Posts.CREATOR + " TEXT, "
                + FeedsDB.Posts.DESCRIPTION + " TEXT);");

        db.execSQL("CREATE TABLE " + FeedsDB.Posts.TABELA_PUBLICO + "("
                + FeedsDB.Posts._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + FeedsDB.Posts.TITLE + " TEXT, "
                + FeedsDB.Posts.LINK + " TEXT UNIQUE, "
                + FeedsDB.Posts.GUID + " TEXT, "
                + FeedsDB.Posts.COMMENTS + " TEXT, "
                + FeedsDB.Posts.PUB_DATE + " INTEGER, "
                + FeedsDB.Posts.CREATOR + " TEXT, "
                + FeedsDB.Posts.DESCRIPTION + " TEXT);");

        db.execSQL("CREATE TABLE " + FeedsDB.Posts.TABELA_EXPRESSO + "("
                + FeedsDB.Posts._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + FeedsDB.Posts.TITLE + " TEXT, "
                + FeedsDB.Posts.LINK + " TEXT UNIQUE, "
                + FeedsDB.Posts.GUID + " TEXT, "
                + FeedsDB.Posts.COMMENTS + " TEXT, "
                + FeedsDB.Posts.PUB_DATE + " INTEGER, "
                + FeedsDB.Posts.CREATOR + " TEXT, "
                + FeedsDB.Posts.DESCRIPTION + " TEXT);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
