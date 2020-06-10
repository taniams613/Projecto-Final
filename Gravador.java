package com.example.a2casopratico;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class Gravador extends Activity implements SensorEventListener {

    private TextView texto;
    private Sensor sensor;
    private SensorManager sensorManager;

    private MediaRecorder gravador;
    private boolean active = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gravador);
        texto = findViewById(R.id.textView);

        findViewById(R.id.bFechar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initSensors() {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        texto.setText(R.string.pronto);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (checkPermits())
            initSensors();
        else
            requestPermits();
    }

    public boolean checkPermits() {
        int r1 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int r2 = ContextCompat.checkSelfPermission(getApplicationContext(), RECORD_AUDIO);

        return r1 == PackageManager.PERMISSION_GRANTED &&
                r2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermits() {
        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, 100);
    }

    @Override
    public void onRequestPermissionsResult (int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)  {
        if (requestCode == 100 && grantResults.length > 0) {
            boolean WriteStoragePermission = grantResults [0] == PackageManager.PERMISSION_GRANTED;
            boolean RecordPermission = grantResults [1] == PackageManager.PERMISSION_GRANTED;

            if (WriteStoragePermission && RecordPermission)
                initSensors();
            else
                texto.setText(R.string.erro_permiss);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sensor != null) {
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
            texto.setText(R.string.pronto);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.values[0] < sensor.getMaximumRange()) {
            if (!active) {
                iniciarGravacao();
            }
        }
        else {
            if (active) {
                pararGravacao();

                Intent intent = new Intent(this, Leitor.class);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    private void iniciarGravacao() {
        gravador = new MediaRecorder();
        gravador.setAudioSource(MediaRecorder.AudioSource.MIC);
        gravador.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        gravador.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        String filename = Environment.getExternalStorageDirectory().getAbsolutePath() + "/test.3gp";
        gravador.setOutputFile(filename);

        try {
            gravador.prepare();
            gravador.start();
            texto.setText(R.string.a_gravar);
            this.active = true;
        }
        catch (IOException ioe) {
            Toast.makeText(getBaseContext(), "Ocorreu um erro ao gravar", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void pararGravacao() {
        gravador.stop();
        gravador.release();
        gravador = null;
        this.active = false;
    }

    @Override
    protected void onStop() {
        if (sensor != null) {
            sensorManager.unregisterListener(this, sensor);
            if (active) {
                pararGravacao();
            }
        }
        super.onStop();
    }

}
