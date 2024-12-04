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
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DashboardActivity extends Activity {

    private MapView mapView;
    private RecyclerView recyclerView;
    private BusAdapter busAdapter;
    private DBHandler dbHandler;

    private Polyline currentRoute;  // Reference to the current route for highlighting

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

        // Initialize the RecyclerView
        recyclerView = findViewById(R.id.recyclerViewBuses);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize DBHandler to fetch data from the database
        dbHandler = new DBHandler(this);

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

                // Handle marker click to display city info and highlight route
                cityMarker.setOnMarkerClickListener((marker, mapView) -> {
                    // Show additional information or perform actions
                    displayCityInfo(marker.getTitle());

                    // If a route already exists, remove it from the map
                    if (currentRoute != null) {
                        mapView.getOverlays().remove(currentRoute);
                    }

                    // Get the selected city coordinates
                    GeoPoint selectedCityPoint = cityCoordinates.get(marker.getTitle());

                    // Choose the destination city to draw the route to (can be another city or fixed destination)
                    GeoPoint destinationPoint = cityCoordinates.get("Colombo"); // Fixed destination to Colombo

                    // Create a new route (polyline)
                    List<GeoPoint> routePoints = new ArrayList<>();
                    routePoints.add(selectedCityPoint); // Start from selected city
                    routePoints.add(destinationPoint); // End at Colombo (or another city)

                    // Draw the route on the map
                    currentRoute = new Polyline();
                    currentRoute.setPoints(routePoints);
                    currentRoute.setWidth(5f); // Set the width of the route line
                    currentRoute.setColor(getResources().getColor(R.color.linkColor)); // Set route line color

                    // Add the polyline to the map
                    mapView.getOverlays().add(currentRoute);

                    // Refresh the map view to display the route
                    mapView.invalidate();

                    return true;
                });

                // Add marker to the map
                mapView.getOverlays().add(cityMarker);
            }
        }

        // Refresh the map view to display markers
        mapView.invalidate();
    }

    // Method to display city information and update the RecyclerView
    private void displayCityInfo(String cityName) {
        dbHandler = new DBHandler(this);
        List<Bus> busList = dbHandler.getAllBuses(cityName);

        busAdapter = new BusAdapter(busList);
        recyclerView.setAdapter(busAdapter);
    }

    // Method to get coordinates of cities
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

    // Method to initiate bus booking
    public void bookBus(View view) {
        Intent intent = new Intent(this, BookBus.class);
        startActivity(intent);
    }
}
