package com.example.a2casopratico;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ProviderPublico extends ProviderBase {

    static {
        uriMatcher.addURI("publico.pt", "post", POST);
        uriMatcher.addURI("publico.pt", "post/#", POST_ID);
    }


    protected String getNomeTabela() {
        return FeedsDB.Posts.TABELA_PUBLICO;
    }

    protected Uri getFeed() {
        if (feed == null) {
            feed = Uri.parse("content://publico.pt/post");
        }
        return feed;
    }


    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {
            case POST:
                return "vnd.android.cursor.dir/vnd.publico.post";
            case POST_ID:
                return "vnd.android.cursor.item/vnd.publico.post";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

}
