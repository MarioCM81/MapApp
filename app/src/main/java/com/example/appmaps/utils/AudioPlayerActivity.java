package com.example.appmaps.utils;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.appmaps.R;

public class AudioPlayerActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audioplayeractivity);

        // Obtener el recurso de audio desde el Intent
        int audioResource = getIntent().getIntExtra("audioResource", -1);

        if (audioResource != -1) {
            // Inicializa el MediaPlayer con el recurso dinámico
            mediaPlayer = MediaPlayer.create(this, audioResource);
            mediaPlayer.start();
        } else {
            // Manejar el caso en el que no se reciba un recurso válido
            Toast.makeText(this, "No se pudo cargar el audio", Toast.LENGTH_SHORT).show();
            finish(); // Cierra la actividad si no hay un audio válido
        }

        // Configura el botón para volver
        Button btnBackToMap = findViewById(R.id.btnBackToMap);
        btnBackToMap.setOnClickListener(v -> finish());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}

