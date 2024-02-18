package com.example.prubbsprites;

import android.graphics.drawable.AnimatedImageDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private ImageView enemigoImageView;
    private AnimatedImageDrawable animatedImageDrawable;
    private int defaultGifResource = R.drawable.ryupose;
    private int newGifResource;
    private int enemigoPosition = 0;
    private int distanciaDanio = 500;
    private int nuevoAncho = 671;
    private int nuevaAltura = 526;
    private int anchoOriginal = 271;
    private int alturaOriginal = 126;
    private int limitePantalla = -1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        defaultGifResource = R.drawable.ryupose;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            animatedImageDrawable = (AnimatedImageDrawable) imageView.getDrawable();
        }

        enemigoImageView = findViewById(R.id.enemigoImageView);
        enemigoImageView.setVisibility(View.VISIBLE);

        enemigoPosition = imageView.getWidth() - enemigoImageView.getWidth();
        enemigoImageView.setTranslationX(enemigoPosition);

        moveEnemigo();

        anchoOriginal = imageView.getLayoutParams().width;
        alturaOriginal = imageView.getLayoutParams().height;

        Button startAnimationButton = findViewById(R.id.startAnimationButton);
        startAnimationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    if (!animatedImageDrawable.isRunning()) {
                        animatedImageDrawable.start();
                    }
                }
            }
        });

        Button moveRightButton = findViewById(R.id.moveRightButton);
        moveRightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float currentTranslationX = imageView.getTranslationX();
                float translationXChange = 20;
                imageView.setTranslationX(currentTranslationX + translationXChange);
            }
        });

        Button moveLeftButton = findViewById(R.id.moveLeftButton);
        moveLeftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float currentTranslationX = imageView.getTranslationX();
                float translationXChange = -20;
                imageView.setTranslationX(currentTranslationX + translationXChange);
            }
        });

        Button golpearButton = findViewById(R.id.golpearButton);
        golpearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadNewGif();

                float enemigoDistance = Math.abs(enemigoImageView.getX() - imageView.getX());
                if (enemigoDistance <= distanciaDanio) {
                    enemigoImageView.setVisibility(View.INVISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            enemigoImageView.setVisibility(View.VISIBLE);
                            resetEnemigoPosition();
                        }
                    }, 1000);
                }
            }
        });
    }

    private void moveEnemigo() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                enemigoPosition -= 20;
                enemigoImageView.setTranslationX(enemigoPosition);

                if (enemigoPosition <= limitePantalla - enemigoImageView.getWidth()) {
                    enemigoPosition = imageView.getWidth() - enemigoImageView.getWidth();
                    enemigoImageView.setTranslationX(enemigoPosition);
                }

                // Comenzar la animación del GIF del enemigo
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    AnimatedImageDrawable animatedEnemyDrawable = (AnimatedImageDrawable) enemigoImageView.getDrawable();
                    if (!animatedEnemyDrawable.isRunning()) {
                        animatedEnemyDrawable.start();
                    }
                }

                moveEnemigo();
            }
        }, 100);
    }

    private void resetEnemigoPosition() {
        enemigoPosition = imageView.getWidth() - enemigoImageView.getWidth();
        enemigoImageView.setTranslationX(enemigoPosition);
    }

    private void loadNewGif() {
        newGifResource = R.drawable.samurr;
        defaultGifResource = R.drawable.samurai;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            if (animatedImageDrawable != null && animatedImageDrawable.isRunning()) {
                animatedImageDrawable.stop();
            }
        }

        ViewGroup.LayoutParams params = imageView.getLayoutParams();
        params.width = nuevoAncho;
        params.height = nuevaAltura;
        imageView.setLayoutParams(params);

        imageView.setImageResource(newGifResource);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            animatedImageDrawable = (AnimatedImageDrawable) imageView.getDrawable();
            animatedImageDrawable.start();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    imageView.setImageResource(defaultGifResource);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        animatedImageDrawable = (AnimatedImageDrawable) imageView.getDrawable();
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        if (!animatedImageDrawable.isRunning()) {
                            animatedImageDrawable.start();
                        }
                    }

                    ViewGroup.LayoutParams params = imageView.getLayoutParams();
                    params.width = anchoOriginal;
                    params.height = alturaOriginal;
                    imageView.setLayoutParams(params);
                }
            }, 900);
        }
    }
}