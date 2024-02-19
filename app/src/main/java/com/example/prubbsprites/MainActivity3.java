package com.example.prubbsprites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

public class MainActivity3 extends AppCompatActivity {

    MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        mediaPlayer = MediaPlayer.create(this, R.raw.ganar);
        mediaPlayer.start();
    }

    public void salir(View view) {
        startActivity(new Intent(MainActivity3.this, MainActivity2.class));
    }
}