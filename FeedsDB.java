package com.example.a2casopratico;

import android.provider.BaseColumns;

public class FeedsDB {
    public static final String DB_NAME = "noticias.db";
    public static final int DB_VERSION = 1;

    private FeedsDB() {
    }

    public static final class Posts implements BaseColumns {
        private Posts() {
        }

        public static final String DEFAULT_SORT_ORDER = "_ID DESC";

        public static final String TABELA_CORREIO = "cmjornal";
        public static final String TABELA_PUBLICO = "publico";
        public static final String TABELA_EXPRESSO = "expresso";

        public static final String TITLE = "title";
        public static final String LINK = "link";
        public static final String GUID = "guid";
        public static final String COMMENTS = "comments";
        public static final String PUB_DATE = "pub_date";
        public static final String CREATOR = "creator";
        public static final String DESCRIPTION = "description";
    }
}
