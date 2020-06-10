package com.example.a2casopratico;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.LinearInterpolator;

import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
    }


    @Override
    protected void onResume() {
        super.onResume();
        ObjectAnimator animator = ObjectAnimator.ofFloat(findViewById(R.id.logo_main), "rotation", 360f);
        animator.setDuration(40000);
        animator.setRepeatCount(ObjectAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.start();
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        Class activity = null;
        switch (item.getItemId()) {
            case R.id.gravador:
                activity = Gravador.class;
                break;
            case R.id.publico: activity = ActivityPublico.class;
                break;
            case R.id.expresso: activity = ActivityExpresso.class;
                break;
            case R.id.correio: activity = ActivityCorreio.class;
                break;
            default:
        }
        if (activity != null) {
            Intent intent = new Intent(this, activity);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
