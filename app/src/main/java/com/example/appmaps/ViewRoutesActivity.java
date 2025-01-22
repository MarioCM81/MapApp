package com.example.appmaps;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.appmaps.data.AppDatabase;
import com.example.appmaps.data.UserLocation;
import java.util.ArrayList;
import java.util.List;

public class ViewRoutesActivity extends AppCompatActivity {

    private ListView listViewRoutes;
    private ArrayAdapter<String> adapter;
    private List<String> routeList;
    private Button btnBackToMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_routes);

        listViewRoutes = findViewById(R.id.listViewRoutes);
        routeList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, routeList);
        listViewRoutes.setAdapter(adapter);

        btnBackToMap = findViewById(R.id.btnBackToMap);
        btnBackToMap.setOnClickListener(v -> finish());

        loadRoutes();
    }

    private void loadRoutes() {
        AppDatabase db = AppDatabase.getDatabase(this);

        new Thread(() -> {
            List<UserLocation> locations = db.userLocationDao().getLocationsForUser(1); // userId = 1 como ejemplo
            runOnUiThread(() -> {
                for (UserLocation location : locations) {
                    routeList.add("Lat: " + location.latitude + ", Lon: " + location.longitude + ", Date: " + location.dateTime);
                }
                adapter.notifyDataSetChanged();
            });
        }).start();
    }
}