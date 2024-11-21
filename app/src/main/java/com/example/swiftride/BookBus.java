package com.example.swiftride;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.List;

public class BookBus extends Activity {

    private String selectedStartPoint;
    private String selectedDestinationPoint;
    private int noSeats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bus_book);

        Spinner startSpinner = findViewById(R.id.selectBusStart);
        Spinner destinationSpinner = findViewById(R.id.selectBusDestination);

        DBHandler dbHelper = new DBHandler(this);
        List<String> startPoint = dbHelper.getStartPoint();
        List<String> destinationPoint = dbHelper.getDestination();



        // Create an ArrayAdapter using the starting points and set it to the spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, startPoint);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        startSpinner.setAdapter(adapter);

        // Set the OnItemSelectedListener to handle item selection in the spinner
        startSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedStartPoint = parentView.getItemAtPosition(position).toString(); // Assign selected NIC to class-level variable
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        // Create an ArrayAdapter using the destination points and set it to the spinner
        ArrayAdapter<String> adapterDestination = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, destinationPoint);
        adapterDestination.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        destinationSpinner.setAdapter(adapterDestination);

        // Set the OnItemSelectedListener to handle item selection in the spinner
        destinationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedStartPoint = parentView.getItemAtPosition(position).toString(); // Assign selected NIC to class-level variable
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
    }
}
