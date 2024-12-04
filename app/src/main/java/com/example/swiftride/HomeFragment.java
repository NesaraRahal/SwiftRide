package com.example.swiftride;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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

public class HomeFragment extends Fragment {

    private MapView mapView;
    private RecyclerView recyclerView;
    private BusAdapter busAdapter;
    private DBHandler dbHandler;
    private Polyline currentRoute;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for the fragment
        View rootView = inflater.inflate(R.layout.dashboard_page, container, false);

        // Configure the map
        Configuration.getInstance().setUserAgentValue(requireContext().getPackageName());

        // Initialize the MapView
        mapView = rootView.findViewById(R.id.map);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setMultiTouchControls(true);
        mapView.getController().setZoom(15.0);

        // Initialize the RecyclerView
        recyclerView = rootView.findViewById(R.id.recyclerViewBuses);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize DBHandler to fetch data from the database
        dbHandler = new DBHandler(getContext());

        // Retrieve distinct cities from the database
        List<String> distinctCities = dbHandler.getDistinctCities();
        HashMap<String, GeoPoint> cityCoordinates = getCityCoordinates();

        // Add markers for each distinct city
        addCityMarkers(distinctCities, cityCoordinates);

        // Setup Book Bus Button
        Button bookBusButton = rootView.findViewById(R.id.bookBus);
        bookBusButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), BookBus.class);
            startActivity(intent);
        });

        return rootView;
    }

    

    // Method to add city markers and handle marker interactions
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
                    highlightRoute(coordinates.get(marker.getTitle()), coordinates.get("Colombo")); // Example route to Colombo
                    return true;
                });

                mapView.getOverlays().add(cityMarker);
            }
        }
        mapView.invalidate();
    }

    // Method to display bus info for the selected city
    private void displayCityInfo(String cityName) {
        List<Bus> busList = dbHandler.getAllBuses(cityName);
        busAdapter = new BusAdapter(busList);
        recyclerView.setAdapter(busAdapter);
    }

    // Method to draw a route on the map
    private void highlightRoute(GeoPoint startPoint, GeoPoint endPoint) {
        if (currentRoute != null) {
            mapView.getOverlays().remove(currentRoute);
        }

        List<GeoPoint> routePoints = new ArrayList<>();
        routePoints.add(startPoint);
        routePoints.add(endPoint);

        currentRoute = new Polyline();
        currentRoute.setPoints(routePoints);
        currentRoute.setWidth(5f);
        currentRoute.setColor(getResources().getColor(R.color.linkColor));

        mapView.getOverlays().add(currentRoute);
        mapView.invalidate();
    }

    // Method to provide coordinates for cities
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
