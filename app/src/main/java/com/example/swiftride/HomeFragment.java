package com.example.swiftride;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeFragment extends Fragment {

    private MapView mapView;
    private RecyclerView recyclerView;
    private BusAdapter busAdapter;
    private DBHandler dbHandler;
    private Polyline currentRoute;

    private final GeoPoint fixedDestination = new GeoPoint(6.936519147052757, 79.84646592471732); // Fixed destination

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dashboard_page, container, false);

        Configuration.getInstance().setUserAgentValue(requireContext().getPackageName());

        // Initialize MapView
        mapView = rootView.findViewById(R.id.map);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setMultiTouchControls(true);
        mapView.getController().setZoom(15.0);

        // Set marker for the fixed destination
        Marker destinationMarker = new Marker(mapView);
        destinationMarker.setPosition(fixedDestination);
        destinationMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        destinationMarker.setIcon(getResources().getDrawable(R.drawable.marker_destination));
        destinationMarker.setTitle("Fixed Destination");
        mapView.getOverlays().add(destinationMarker);

        // Initialize RecyclerView for bus list
        recyclerView = rootView.findViewById(R.id.recyclerViewBuses);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        dbHandler = new DBHandler(getContext());
        List<String> distinctCities = dbHandler.getDistinctCities();
        HashMap<String, GeoPoint> cityCoordinates = getCityCoordinates();

        addCityMarkers(distinctCities, cityCoordinates);

        // Book Bus button functionality
        Button bookBusButton = rootView.findViewById(R.id.bookBus);
        bookBusButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), BookBus.class);
            startActivity(intent);
        });

        return rootView;
    }

    // Add markers for each city on the map
    private void addCityMarkers(List<String> cities, HashMap<String, GeoPoint> coordinates) {
        for (String city : cities) {
            if (coordinates.containsKey(city)) {
                GeoPoint cityPoint = coordinates.get(city);

                Marker cityMarker = new Marker(mapView);
                cityMarker.setPosition(cityPoint);
                cityMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                cityMarker.setIcon(getResources().getDrawable(R.drawable.ic_location));
                cityMarker.setTitle(city);

                cityMarker.setOnMarkerClickListener((marker, mapView) -> {
                    displayCityInfo(marker.getTitle());
                    drawRouteToDestination(marker.getTitle(), coordinates);
                    return true;
                });

                mapView.getOverlays().add(cityMarker);
            }
        }
        mapView.invalidate();
    }

    // Display bus list for selected city
    private void displayCityInfo(String cityName) {
        List<Bus> busList = dbHandler.getAllBuses(cityName);
        busAdapter = new BusAdapter(busList);
        recyclerView.setAdapter(busAdapter);
    }

    // Draw route from city to the fixed destination
    private void drawRouteToDestination(String locationName, HashMap<String, GeoPoint> coordinates) {
        GeoPoint startPoint = coordinates.get(locationName);
        if (startPoint == null) {
            Log.e("HomeFragment", "Cannot find route: location not found.");
            return;
        }

        String osrmUrl = String.format(
                "https://router.project-osrm.org/route/v1/driving/%f,%f;%f,%f?overview=full&geometries=geojson",
                startPoint.getLongitude(), startPoint.getLatitude(),
                fixedDestination.getLongitude(), fixedDestination.getLatitude()
        );

        new Thread(() -> {
            try {
                URL url = new URL(osrmUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    JSONObject jsonResponse = new JSONObject(response.toString());
                    JSONArray coordinatesArray = jsonResponse.getJSONArray("routes")
                            .getJSONObject(0)
                            .getJSONObject("geometry")
                            .getJSONArray("coordinates");

                    List<GeoPoint> routePoints = new ArrayList<>();
                    for (int i = 0; i < coordinatesArray.length(); i++) {
                        JSONArray point = coordinatesArray.getJSONArray(i);
                        double lon = point.getDouble(0);
                        double lat = point.getDouble(1);
                        routePoints.add(new GeoPoint(lat, lon));
                    }

                    requireActivity().runOnUiThread(() -> {
                        if (currentRoute != null) {
                            mapView.getOverlays().remove(currentRoute);
                        }

                        currentRoute = new Polyline();
                        currentRoute.setPoints(routePoints);
                        currentRoute.setColor(getResources().getColor(R.color.linkColor));
                        currentRoute.setWidth(10f);

                        mapView.getOverlays().add(currentRoute);
                        mapView.invalidate();
                    });

                } else {
                    Log.e("HomeFragment", "Failed to fetch route data: " + connection.getResponseCode());
                }
            } catch (Exception e) {
                Log.e("HomeFragment", "Error fetching route data", e);
            }
        }).start();
    }

    // Get coordinates for predefined cities
    private HashMap<String, GeoPoint> getCityCoordinates() {
        HashMap<String, GeoPoint> cityCoordinates = new HashMap<>();
        cityCoordinates.put("Colombo", new GeoPoint(6.9271, 79.8612));
        cityCoordinates.put("Kandy", new GeoPoint(7.2906, 80.6337));
        cityCoordinates.put("Galle", new GeoPoint(6.0367, 80.217));
        cityCoordinates.put("Jaffna", new GeoPoint(9.6615, 80.0255));
        cityCoordinates.put("Matara", new GeoPoint(5.9485, 80.5353));
        return cityCoordinates;
    }
}
