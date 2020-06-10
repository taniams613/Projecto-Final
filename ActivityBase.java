package com.example.a2casopratico;

import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Date;
import java.util.Objects;

public abstract class ActivityBase extends ListActivity {
    protected static final long FREQUENCIA_ATUALIZACAO = 10 * 60 * 1000;

    private Cursor cursor;

    protected AtualizarPostAsyncTask tarefa;

    protected Uri baseUri = null;
    protected abstract Uri getBaseUri();

    protected Uri dbUri = null;
    protected abstract Uri getDBUri();

    protected String URL = null;
    protected abstract String getURL();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_feed);
        configurarAdapter();
    }


    protected void configurarAdapter() {
        final String[] colunas = new String[] { FeedsDB.Posts._ID, FeedsDB.Posts.TITLE, FeedsDB.Posts.LINK, FeedsDB.Posts.GUID, FeedsDB.Posts.PUB_DATE };

        cursor = managedQuery(getBaseUri(), colunas, null, null, FeedsDB.Posts.PUB_DATE + " DESC");
        cursor.setNotificationUri(getContentResolver(), getBaseUri());

        String[] camposDb = new String[] { FeedsDB.Posts.TITLE, FeedsDB.Posts.PUB_DATE };
        int[] camposView = new int[] { R.id.feedTitulo, R.id.feedData };

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.feed_item, cursor, camposDb, camposView, 0);

        final java.text.DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.LONG);

        adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                if (view.getId() == R.id.feedData) {
                    long timeStamp = cursor.getLong(columnIndex);
                    ((TextView)view).setText(dateFormat.format(timeStamp));
                    return true;
                }
                else {
                    return false;
                }
            }
        });

        setListAdapter(adapter);
    }



    @Override
    protected void onStart() {
        super.onStart();
        ActionBar ab = Objects.requireNonNull(getActionBar());
        ab.setHomeButtonEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return onOptionsItemSelected(item);
    }


    public void onResume() {
        super.onResume();
        carregarNoticias();
    }

    private void carregarNoticias() {
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        long ultima = prefs.getLong("ultima_atualizacao", 0);

        if ((System.currentTimeMillis() - ultima) > FREQUENCIA_ATUALIZACAO) {
            tarefa = new AtualizarPostAsyncTask();
            tarefa.execute();
        }
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        cursor. moveToPosition(position);
        Intent i = new Intent(this, VerNoticia.class);
        i.putExtra("link", cursor.getString(cursor.getColumnIndex(FeedsDB.Posts.LINK)));
        i.putExtra("guid", cursor.getString(cursor.getColumnIndex(FeedsDB.Posts.GUID)));
        i.putExtra("title", cursor.getString(cursor.getColumnIndex(FeedsDB.Posts.TITLE)));
        startActivity(i);
    }

    protected void onStop() {
        if (tarefa != null && !tarefa.getStatus().equals(AsyncTask.Status.FINISHED)) {
            tarefa.cancel(true);
        }
        super.onStop();
    }


    private class AtualizarPostAsyncTask extends AsyncTask<Void, Void, Void> {
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            RssDownloadHelper.updateRssData(getURL(), getContentResolver(), getDBUri(), getPreferences(MODE_PRIVATE));
            return null;
        }

        protected void onPostExecute(Void result) {
            SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
            editor.putLong("ultima_atualizacao", System.currentTimeMillis());
            editor.apply();
            configurarAdapter();
        }

        protected void onCancelled() {
            SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
            editor.putLong("ultima_atualizacao", 0);
            editor.apply();
            super.onCancelled();
        }
    }
}
