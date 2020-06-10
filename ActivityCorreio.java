package com.example.a2casopratico;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

public class ActivityCorreio extends ActivityBase {

    @Override
    protected Uri getBaseUri() {
        if (baseUri == null) {
            baseUri = Uri.parse("content://cmjornal.pt");
        }
        return baseUri;
    }

    @Override
    protected Uri getDBUri() {
        if (dbUri == null) {
            dbUri = Uri.parse("content://cmjornal.pt/" + FeedsDB.Posts.TABELA_CORREIO);
        }
        return dbUri;
    }

    @Override
    protected String getURL() {
        if (URL == null) {
            URL = "https://www.cmjornal.pt/rss";
        }
        return URL;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Correio da Manhã");

        SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
        editor.putString(getDBUri().toString(), "ISO-8859-1");
        editor.apply();
    }


}
