package com.example.a2casopratico;

import android.net.Uri;
import android.os.Bundle;

public class ActivityExpresso extends ActivityBase {

    @Override
    protected Uri getBaseUri() {
        if (baseUri == null) {
            baseUri = Uri.parse("content://expresso.pt");
        }
        return baseUri;
    }

    @Override
    protected Uri getDBUri() {
        if (dbUri == null) {
            dbUri = Uri.parse("content://expresso.pt/" + FeedsDB.Posts.TABELA_EXPRESSO);
        }
        return dbUri;
    }

    @Override
    protected String getURL() {
        if (URL == null) {
            URL = "https://feeds.feedburner.com/expresso-geral?format=rss";
        }
        return URL;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Expresso");
    }
}
