package com.example.a2casopratico;

import android.net.Uri;
import android.os.Bundle;

public class ActivityPublico extends ActivityBase {

    @Override
    protected Uri getBaseUri() {
        if (baseUri == null) {
            baseUri = Uri.parse("content://publico.pt");
        }
        return baseUri;
    }

    @Override
    protected Uri getDBUri() {
        if (dbUri == null) {
            dbUri = Uri.parse("content://publico.pt/" + FeedsDB.Posts.TABELA_PUBLICO);
        }
        return dbUri;
    }

    @Override
    protected String getURL() {
        if (URL == null) {
            URL = "https://feeds.feedburner.com/PublicoRSS?format=rss";
        }
        return URL;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Publico");
    }
}
