package com.example.appmaps;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.appmaps.data.AppDatabase;
import com.example.appmaps.data.UserLocation;
import com.example.appmaps.data.UserLocationDao;
import com.example.appmaps.utils.AudioPlayerActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final float ZONE_RADIUS_METERS = 20.0f; // Radio de la zona
    private static final LatLng valenciaLocation = new LatLng(39.48355236594489, -0.37414223722180157);
    private static final LatLng valenciaLocation2 = new LatLng(39.46986202334731, -0.37638783628735056);

    private GoogleMap myMap;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private Polyline polyline;
    private boolean isRecording = false;
    private boolean valenciaLocationVisited = false;
    private boolean valenciaLocationExited = true;
    private boolean valenciaLocation2Visited = false;
    private boolean valenciaLocation2Exited = true;
    private ViewPager2 viewPager2;
    private List<Slide> slides; // Declare slides as a class-level variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Crear y manejar el toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Initialize slides
        slides = new ArrayList<>();
        slides.add(new Slide(R.drawable.ic_instruction1, "Bienvenido", "Explora el mundo con Mapasear."));
        slides.add(new Slide(R.drawable.ic_instruction2, "Dibuja", "Dibuja sobre el mapa tus paseos."));
        slides.add(new Slide(R.drawable.ic_instruction3, "Escucha", "Descubre audios sobre nuevos rincones de la ciudad."));

        // Register the page change callback
        if (viewPager2 != null) {
            // Puedes registrar el callback aquí
            viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    // Tu lógica aquí
                }
            });
        } else {
            Log.e("MainActivity", "ViewPager2 is null!");
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        findViewById(R.id.btnStartRecording).setOnClickListener(v -> startRecording());
        findViewById(R.id.btnStopRecording).setOnClickListener(v -> stopRecording());
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    addPointToPolyline(latLng);
                    myMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    // Guardar la ubicación en la base de datos
                    String dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
                    saveUserLocation(1, latLng.latitude, latLng.longitude, dateTime); // userId = 1 como ejemplo
                }
            }
        };

        checkLocationPermissions();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        myMap = googleMap;

        // Establecer el tipo de mapa
        myMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        //Situar la cámara correctamente
        // Establecer el zoom inicial y la posición del mapa
// Obtener la ubicación actual y mover la cámara a esa ubicación
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                        float zoomLevel = 17.0f; // Puedes ajustar el nivel de zoom según sea necesario
                        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, zoomLevel));
                    }
                });


        float initialZoom = 15.0f;  // Establecer un nivel de zoom apropiado (ejemplo 15)


        // Aplicar estilo al mapa (si es necesario)
        try {
            InputStream inputStream = getResources().openRawResource(R.raw.map_style);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            inputStream.close();
            String style = stringBuilder.toString();
            googleMap.setMapStyle(new MapStyleOptions(style));
        } catch (IOException e) {
            Log.e("MapStyle", "No se pudo cargar el archivo de estilo.");
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            myMap.setMyLocationEnabled(true);
        }

        // Configurar marcadores interactivos
        setupMarkers();

        // Listener para clics en marcadores. Esto 'escuchará' si algún usuario clica en el marcador.
        myMap.setOnMarkerClickListener(marker -> {
            // Obtener la ubicación actual
            fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                if (location != null) {
                    // Obtener la posición del usuario
                    LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());

                    // Calcular la distancia entre el usuario y el marcador (baliza)
                    float distance = calculateDistance(currentLocation, marker.getPosition());

                    if (distance <= 25) {
                        // Si el usuario está dentro del radio de 25 metros, permitir el acceso al audio
                        showAudioDialog(marker);
                    } else {
                        // Si el usuario está fuera del radio de 25 metros, mostrar un mensaje informativo
                        showOutOfRangeDialog();
                    }
                } else {
                    // Si no se puede obtener la ubicación, mostrar un mensaje de error
                    Toast.makeText(this, "No se pudo obtener la ubicación actual", Toast.LENGTH_SHORT).show();
                }
            });
            return true; // True para indicar que el clic fue manejado
        });

        //Esto es opcional, sólo si quieres que aparezca el control de zoom con botoncitos sobre el mapa. Como en el Google Maps.
        myMap.getUiSettings().setZoomControlsEnabled(false);
    }

    //Esto sirve para definir la ubicación y naturaleza de los marcadores
    private void setupMarkers() {
        // Añadir marcadores
        myMap.addMarker(new MarkerOptions()
                .position(valenciaLocation)
                .title("Edificio Misterioso")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

        myMap.addMarker(new MarkerOptions()
                .position(valenciaLocation2)
                .title("La Plaza del Ayuntamiento")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
    }

    //Método para calcular distancia entre dos puntos
    //Lo usaremos para saber si el usuario puede o no intentar reproducir el audio asociado a la baliza que quiere visitar
    private float calculateDistance(LatLng point1, LatLng point2) {
        float[] results = new float[1];
        Location.distanceBetween(
                point1.latitude, point1.longitude,
                point2.latitude, point2.longitude,
                results
        );
        return results[0];
    }

    //Mensaje para pedirle al usuario que se acerque más a la baliza
    private void showOutOfRangeDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Fuera de rango")
                .setMessage("Debe estar dentro de un radio de 25 metros para interactuar con esta baliza.")
                .setPositiveButton("Aceptar", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void showAudioDialog(Marker marker) {
        // Crear cuadro de diálogo
        new AlertDialog.Builder(this)
                .setTitle(marker.getTitle())
                .setMessage("¿Desea usted oír el audio de esta ubicación?")
                .setPositiveButton("Aceptar  ", (dialog, which) -> {
                    // Iniciar actividad de reproducción de audio
                    Intent intent = new Intent(this, AudioPlayerActivity.class);
                    if (marker.getPosition().equals(valenciaLocation)) {
                        intent.putExtra("audioResource", R.raw.prueba); // Audio para valenciaLocation
                    } else if (marker.getPosition().equals(valenciaLocation2)) {
                        intent.putExtra("audioResource", R.raw.plaza_ayuntamiento); // Audio para valenciaLocation2
                    }
                    startActivity(intent);
                })
                .setNegativeButton("  Rechazar", (dialog, which) -> {
                    // Cerrar cuadro de diálogo
                    dialog.dismiss();
                })
                .show();
    }

    private void startRecording() {
        if (polyline != null) {
            polyline.remove();
        }

        // Cambiar el color del recorrido a amarillo cálido
        polyline = myMap.addPolyline(new PolylineOptions().clickable(true).color(Color.parseColor("#FFEB3B")).width(10));
        isRecording = true;

        // Obtener el botón de "Start Recording"
        Button startButton = findViewById(R.id.btnStartRecording);

        // Cambiar el color del texto a gris y deshabilitar el botón
        startButton.setEnabled(false);
        startButton.setTextColor(Color.WHITE);  // Cambiar el color del texto a gris

        // Cambiar el color de fondo del botón a un color más claro cuando está deshabilitado (opcional)
        //startButton.setBackgroundColor(Color.parseColor(String.valueOf(Color.rgb(30,58,95))); //255, 191, 0
        startButton.setBackgroundColor(Color.rgb(255, 191, 0));

        // Habilitar el botón de "Stop Recording"
        Button stopButton = findViewById(R.id.btnStopRecording);
        stopButton.setEnabled(true);

        LocationRequest locationRequest = LocationRequest.create()
                .setInterval(10000)
                .setFastestInterval(5000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
        }
    }

    private void stopRecording() {
        isRecording = false;
        fusedLocationClient.removeLocationUpdates(locationCallback);

        // Obtener el botón de "Start Recording"
        Button startButton = findViewById(R.id.btnStartRecording);

        // Restaurar el color del texto a blanco y habilitar el botón
        startButton.setEnabled(true);
        startButton.setTextColor(Color.WHITE);  // Restaurar el color del texto a blanco

        // Restaurar el color de fondo a su valor original (opcional) //30,58,95
        //startButton.setBackgroundColor(Color.parseColor(String.valueOf(Color.rgb(30,58,95)));
        startButton.setBackgroundColor(Color.rgb(30, 58, 95)); // Sets the background to RGB(30, 58, 95)

        // Deshabilitar el botón de "Stop Recording"
        Button stopButton = findViewById(R.id.btnStopRecording);
        stopButton.setEnabled(false);

        if (polyline != null) {
            savePolyline(polyline.getPoints());
        }
    }

    private void checkLocationPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private void addPointToPolyline(LatLng latLng) {
        if (polyline != null) {
            List<LatLng> points = new ArrayList<>(polyline.getPoints());
            points.add(latLng);
            polyline.setPoints(points);
        }
    }

    private void savePolyline(List<LatLng> points) {
        // Guardar en base de datos o archivo según sea necesario
    }

    private void saveUserLocation(int userId, double latitude, double longitude, String dateTime) {
        AppDatabase db = AppDatabase.getDatabase(this);
        UserLocationDao dao = db.userLocationDao();

        new Thread(() -> {
            UserLocation location = new UserLocation(userId, latitude, longitude, dateTime);
            dao.insert(location);
        }).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflar el menú desde main_menu.xml
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true; // Retorna true para que el menú se muestre
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_view_routes) {
            // Iniciar la actividad para ver rutas anteriores
            Intent intent = new Intent(this, ViewRoutesActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_about) {
            // Crear el AlertDialog
            new android.app.AlertDialog.Builder(this)
                    .setTitle("...\n Acerca de...")  // Título de la ventana emergente
                    .setMessage("Esta APP ha sido realizada por Mario CM y Mirelle CS, para el proyecto final de Linkia FP. Usando en botón 'Start Recording' se dibuja una línea que une los puntos por lo que de desplaza el usuario, cada 5 segundos detectados por GPS. Stop recording finaliza el dibujo.  Estando cerca de algún marcador de posición del mapa, se puede escuchar un audio asociado al mismo si está a menos de 25 metros tocando sobre su indicador en la pantalla.")  // Mensaje que quieres mostrar
                    .setPositiveButton("Cerrar", (dialog, which) -> {
                        // Aquí puedes poner acciones cuando el usuario presione "Aceptar"
                        dialog.dismiss();  // Cierra el diálogo
                    })
                    .show();  // Muestra el diálogo
            Log.i(TAG, "Acaba de abrirse el menú");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permiso de ubicación concedido", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Permiso de ubicación denegado", Toast.LENGTH_LONG).show();
        }
    }

}