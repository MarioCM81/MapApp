package com.example.appmaps;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private LinearLayout indicators;
    private List<Slide> slides; // Variable de instancia para las diapositivas

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        viewPager = findViewById(R.id.viewPager);
        indicators = findViewById(R.id.pageIndicators);

        // Inicializa las diapositivas
        slides = new ArrayList<>();
        slides.add(new Slide(R.drawable.ic_instruction1, "Bienvenido", "Explora el mundo con Mapasear."));
        slides.add(new Slide(R.drawable.ic_instruction2, "Dibuja", "Dibuja sobre el mapa tus paseos."));
        slides.add(new Slide(R.drawable.ic_instruction3, "Escucha", "Descubre audios sobre nuevos rincones de la ciudad."));


        ViewPagerAdapter adapter = new ViewPagerAdapter(this, slides);
        viewPager.setAdapter(adapter);

        // Agrega indicadores dinámicamente
        addIndicators(slides.size());

        // Encuentra el botón "Comenzar"
        Button startButton = findViewById(R.id.start_button);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                updateIndicators(position);

                // Muestra el botón "Comenzar" solo en la última página
                if (position == slides.size() - 1) {
                    startButton.setVisibility(View.VISIBLE);  // Hacer visible solo en la última página
                  //  Toast.makeText(SplashActivity.this, "Botón Visible", Toast.LENGTH_SHORT).show();  // Muestra un Toast
                } else {
                    startButton.setVisibility(View.GONE);  // Mantener oculto en las otras páginas
                }
            }
        });



        // Configura el listener del botón "Comenzar"
        startButton.setOnClickListener(v -> {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Opcional: cerrar la actividad actual
        });
    }

    private void addIndicators(int count) {
        for (int i = 0; i < count; i++) {
            View dot = new View(this);
            dot.setBackgroundResource(R.drawable.indicator_unselected); // Usa un drawable para los puntos
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(20, 20);
            params.setMargins(10, 0, 10, 0);
            indicators.addView(dot, params);
        }

        // Cambiar el indicador activo en el ViewPager2
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                updateIndicators(position);
            }
        });
    }

    private void updateIndicators(int position) {
        for (int i = 0; i < indicators.getChildCount(); i++) {
            View dot = indicators.getChildAt(i);
            dot.setBackgroundResource(i == position
                    ? R.drawable.indicator_selected
                    : R.drawable.indicator_unselected);
        }
    }
}

/*package com.example.appmaps;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SplashActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private LinearLayout indicators;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        viewPager = findViewById(R.id.viewPager);
        indicators = findViewById(R.id.pageIndicators);

        List<Slide> slides = new ArrayList<>();
        slides.add(new Slide(R.drawable.ic_instruction1, "Bienvenido", "Explora el mundo con Mapasear."));
        slides.add(new Slide(R.drawable.ic_instruction2, "Graba", "Guarda tus rutas favoritas."));
        slides.add(new Slide(R.drawable.ic_instruction3, "Comparte", "Muéstrales a tus amigos tus descubrimientos."));

        ViewPagerAdapter adapter = new ViewPagerAdapter(this, slides);
        viewPager.setAdapter(adapter);

        // Agrega indicadores dinámicamente
        addIndicators(slides.size());

        // Encuentra el botón en el layout
        Button startButton = findViewById(R.id.start_button);

    }

    private void addIndicators(int count) {
        for (int i = 0; i < count; i++) {
            View dot = new View(this);
            dot.setBackgroundResource(R.drawable.indicator_unselected); // Usa un drawable para los puntos
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(20, 20);
            params.setMargins(10, 0, 10, 0);
            indicators.addView(dot, params);
        }

        // Cambiar el indicador activo en el ViewPager2
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                updateIndicators(position);
            }
        });
    }

    private void updateIndicators(int position) {
        for (int i = 0; i < indicators.getChildCount(); i++) {
            View dot = indicators.getChildAt(i);
            dot.setBackgroundResource(i == position
                    ? R.drawable.indicator_selected
                    : R.drawable.indicator_unselected);
        }
    }
}*/
