package com.example.swiftride;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DashboardActivity extends Activity {

    private MapView mapView;
    private RecyclerView recyclerView;
    private BusAdapter busAdapter;
    private DBHandler dbHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Configuration.getInstance().setUserAgentValue("com.testing.bussbooking");
        setContentView(R.layout.dashboard_page);

        // Initialize the MapView
        mapView = findViewById(R.id.map);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setMultiTouchControls(true);


        mapView.getController().setZoom(15.0);


        recyclerView = findViewById(R.id.recyclerViewBuses);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbHandler = new DBHandler(this);
        List<Bus> busList = dbHandler.getAllBuses();

        busAdapter = new BusAdapter(busList);
        recyclerView.setAdapter(busAdapter);

        // Retrieve distinct cities from the database
        List<String> distinctCities = dbHandler.getDistinctCities();
        HashMap<String, GeoPoint> cityCoordinates = getCityCoordinates();

        // Add markers for each distinct city
        for (String city : distinctCities) {
            if (cityCoordinates.containsKey(city)) {
                GeoPoint cityPoint = cityCoordinates.get(city);

                Marker cityMarker = new Marker(mapView);
                cityMarker.setPosition(cityPoint);
                cityMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                cityMarker.setIcon(getResources().getDrawable(R.drawable.ic_location));
                cityMarker.setTitle(city);
                cityMarker.setSnippet("Marker for " + city);

                // Optional: Add click behavior for the marker
                cityMarker.setOnMarkerClickListener((marker, mapView) -> {
                    // Show additional information or perform actions
                    return true;
                });

                // Add marker to the map
                mapView.getOverlays().add(cityMarker);
            }
        }

        // Refresh the map view to display markers
        mapView.invalidate();
    }

    private HashMap<String, GeoPoint> getCityCoordinates() {
        HashMap<String, GeoPoint> cityCoordinates = new HashMap<>();
        cityCoordinates.put("Colombo", new GeoPoint(6.9271, 79.8612));
        cityCoordinates.put("Kandy", new GeoPoint(7.2906, 80.6337));
        cityCoordinates.put("Galle", new GeoPoint(6.0367, 80.217));
        cityCoordinates.put("Jaffna", new GeoPoint(9.6615, 80.0255));
        cityCoordinates.put("Matara", new GeoPoint(5.9485, 80.5353));
        // Add more cities as needed
        return cityCoordinates;
    }

}




