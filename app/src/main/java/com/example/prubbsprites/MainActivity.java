package com.example.prubbsprites;

import android.graphics.drawable.AnimatedImageDrawable;
import android.media.MediaPlayer;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;

public class MainActivity extends AppCompatActivity {

    MediaPlayer media;
    private Handler handler = new Handler();
    MediaPlayer media_sd;
    public int contadores = 10;
    private TextView  contador2;
    public  ImageView imageView;
    private ImageView enemigoImageView;
    private AnimatedImageDrawable animatedImageDrawable;
    private int defaultGifResource = R.drawable.asra;
    private int newGifResource;
    private int enemigoPosition = 0;
    private int distanciaDanio = 500;
    private int nuevoAncho = 671;
    private int nuevaAltura = 526;
    private int anchoOriginal = 271;
    private int alturaOriginal = 126;
    private int limitePantalla = -1500;
    private int nuevoAnchoMovimiento = 450; // Nuevo ancho del GIF durante el movimiento
    private int nuevaAlturaMovimiento = 350; // Nueva altura del GIF durante el movimiento
    private boolean isMoving = false; // Indica si el personaje está en movimiento
    private static final int MOVEMENT_DURATION = 1000; // Duración del movimiento en milisegundos
    private float currentTranslationX = 0; // Posición actual de la imagen

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contador2 = findViewById(R.id.contador);
        if (media == null) {
            media = MediaPlayer.create(this, R.raw.sound);
        }

        if ((!media.isPlaying())) {
            media.start();
        }

        imageView = findViewById(R.id.imageView);
        defaultGifResource = R.drawable.asra;
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
                // Cambiar el GIF del personaje al mover a la derecha
                //loadNewGif(R.drawable.asrandar, nuevoAnchoMovimiento, nuevaAlturaMovimiento);
                isMoving = true;

                currentTranslationX += 20; // Incrementar la posición actual
                imageView.setTranslationX(currentTranslationX);
            }
        });

        Button moveLeftButton = findViewById(R.id.moveLeftButton);
        moveLeftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cambiar el GIF del personaje al mover a la izquierda
                loadNewGif(R.drawable.asrandar, nuevoAnchoMovimiento, nuevaAlturaMovimiento);
                isMoving = true;

                currentTranslationX -= 30; // Decrementar la posición actual
                imageView.setTranslationX(currentTranslationX);
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
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (contadores > 0) {
                    try {
                        // Pausar el hilo durante 1 segundo
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            updateCounter();
                        }
                    });
                }
            }
        }).start();
    }
    private void updateCounter() {

        contadores--;
        if (contadores >= 0) {
            if (contadores < 10) {
                contador2.setText("0" + String.valueOf(contadores));
            } else {
                contador2.setText(String.valueOf(contadores));
            }
        }
    }

    private void moveEnemigo() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                enemigoPosition -= 30;
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
        newGifResource = R.drawable.asrattak;
        defaultGifResource = R.drawable.asra;

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

                    // Restablecer la posición original después de un tiempo
                    resetImageViewPosition();
                }
            }, 900);
        }
    }

    private void loadNewGif(int resource, int newWidth, int newHeight) {
        newGifResource = resource;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            if (animatedImageDrawable != null && animatedImageDrawable.isRunning()) {
                animatedImageDrawable.stop();
            }
        }

        ViewGroup.LayoutParams params = imageView.getLayoutParams();
        params.width = newWidth; // Usar el nuevo ancho proporcionado
        params.height = newHeight; // Usar la nueva altura proporcionada
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
                        if (animatedImageDrawable != null && animatedImageDrawable.isRunning()) {
                            animatedImageDrawable.stop();
                        }
                    }

                    ViewGroup.LayoutParams params = imageView.getLayoutParams();
                    params.width = anchoOriginal;
                    params.height = alturaOriginal;
                    imageView.setLayoutParams(params);

                    // Restablecer la posición original después de un tiempo
                    resetImageViewPosition();
                }
            }, 500); // Cambiar el tiempo de espera a 500 milisegundos (0.5 segundos)
        }
    }

    private void resetImageViewPosition() {
        // Restablecer la posición original de la vista imageView
        imageView.setTranslationX(currentTranslationX); // Mantener la posición actual
        isMoving = false;
    }
}
