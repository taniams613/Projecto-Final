package com.example.a2casopratico;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

public class Leitor extends Activity implements MediaPlayer.OnCompletionListener, SensorEventListener {

    private MediaPlayer leitor;
    private TextView texto;
    private Button bPlay;

    private Sensor sensor;
    private SensorManager sensorManager;
    private long lastUpdate = 0;
    private float lastSum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leitor);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        texto = findViewById(R.id.mensagem);

        bPlay = findViewById(R.id.bPlay);
        bPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (leitor == null)
                    return;
                if (leitor.isPlaying())
                    pararReproducao();
                else
                    iniciarReproducao();
            }
        });

        findViewById(R.id.bFechar2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        activarSensor();
        initPlayer();
    }

    private void initPlayer() {
        leitor = new MediaPlayer();
        leitor.setOnCompletionListener(this);
        String filename = Environment.getExternalStorageDirectory().getAbsolutePath() + "/test.3gp";
        try {
            leitor.setDataSource(this, Uri.parse(filename));
            leitor.prepare();
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private void iniciarReproducao() {
        desactivarSensor();
        leitor.start();
        texto.setText(R.string.a_tocar);
        bPlay.setText(R.string.parar);
    }

    private void pararReproducao() {
        leitor.pause();
        leitor.seekTo(0);
        activarSensor();
        texto.setText(R.string.abaname);
        bPlay.setText(R.string.reproduzir);
    }

    @Override
    protected void onStop() {
        if (leitor != null) {
            pararReproducao();
            leitor.release();
            leitor = null;
        }
        desactivarSensor();
        super.onStop();
    }

    private void activarSensor() {
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);
    }

    private void desactivarSensor() {
        sensorManager.unregisterListener(this, sensor);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        long curTime = System.currentTimeMillis();
        if ((curTime - lastUpdate) > 100) {
            long diffTime = (curTime - lastUpdate);
            lastUpdate = curTime;

            float sum = event.values[0] + event.values[1] + event.values[2];
            float speed = Math.abs(sum - lastSum) / diffTime * 1000;
            lastSum = sum;

            if (speed > 800) {
                // a shake
                iniciarReproducao();
                lastUpdate = 0;
                lastSum = 0;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) { }

    @Override
    public void onCompletion(MediaPlayer mp) {
        pararReproducao();
        activarSensor();
    }
}
