package com.example.a2casopratico;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ProviderExpresso extends ProviderBase {

    static {
        uriMatcher.addURI("expresso.pt", "post", POST);
        uriMatcher.addURI("expresso.pt", "post/#", POST_ID);
    }


    protected String getNomeTabela() {
        return FeedsDB.Posts.TABELA_EXPRESSO;
    }

    protected Uri getFeed() {
        if (feed == null) {
            feed = Uri.parse("content://expresso.pt/post");
        }
        return feed;
    }


    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {
            case POST:
                return "vnd.android.cursor.dir/vnd.expresso.post";
            case POST_ID:
                return "vnd.android.cursor.item/vnd.expresso.post";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

}
