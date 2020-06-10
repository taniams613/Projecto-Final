package com.example.a2casopratico;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ProviderCorreio extends ProviderBase {

    static {
        uriMatcher.addURI("cmjornal.pt", "post", POST);
        uriMatcher.addURI("cmjornal.pt", "post/#", POST_ID);
    }


    protected String getNomeTabela() {
        return FeedsDB.Posts.TABELA_CORREIO;
    }

    protected Uri getFeed() {
        if (feed == null) {
            feed = Uri.parse("content://cmjornal.pt/post");
        }
        return feed;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {
            case POST:
                return "vnd.android.cursor.dir/vnd.cmjornal.post";
            case POST_ID:
                return "vnd.android.cursor.item/vnd.cmjornal.post";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

}
