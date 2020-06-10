package com.example.a2casopratico;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;

import java.util.Objects;

public class VerNoticia extends Activity {

    private WebView conteudo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_noticia);

        conteudo = findViewById(R.id.feedConteudo);
        conteudo.getSettings().setLoadWithOverviewMode(true);
        conteudo.getSettings().setUseWideViewPort(true);
    }

    protected void onStart() {
        super.onStart();

        ActionBar ab = Objects.requireNonNull(getActionBar());
        ab.setHomeButtonEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);
    }

    protected void onResume() {
        super.onResume();
        Bundle extras = getIntent().getExtras();
        setTitle(extras.getString("title"));

        if (Objects.requireNonNull(extras.getString("guid")).startsWith("http"))
            conteudo.loadUrl(extras.getString("guid"));
        else
            conteudo.loadUrl(extras.getString("link"));
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return onOptionsItemSelected(item);
    }
}