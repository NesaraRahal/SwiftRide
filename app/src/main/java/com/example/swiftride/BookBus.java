package com.example.swiftride;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class BookBus extends Activity {

    private String selectedStartPoint;
    private String selectedDestinationPoint;
    private String selectedTimeSlot; // New variable to store selected time slot
    private int noSeats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bus_book);

        // Initialize spinners and TextView
        Spinner startSpinner = findViewById(R.id.selectBusStart);
        Spinner destinationSpinner = findViewById(R.id.selectBusDestination);
        Spinner timeSlotsSpinner = findViewById(R.id.selectBusTime);
        TextView noOfBusTxt = findViewById(R.id.no_seats);

        // Initialize database helper
        DBHandler dbHelper = new DBHandler(this);

        // Retrieve data from the database
        List<String> startPoint = dbHelper.getStartPoint();
        List<String> destinationPoint = dbHelper.getDestination();
        List<String> timeSlots = dbHelper.getDistinctTimeSlots();
        int noSeats = dbHelper.getNoSeats();

        // Update number of seats
        noOfBusTxt.setText(String.valueOf(noSeats));

        // Set up start point spinner
        ArrayAdapter<String> adapterStart = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, startPoint);
        adapterStart.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        startSpinner.setAdapter(adapterStart);

        startSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedStartPoint = parentView.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        // Set up destination point spinner
        ArrayAdapter<String> adapterDestination = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, destinationPoint);
        adapterDestination.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        destinationSpinner.setAdapter(adapterDestination);

        destinationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedDestinationPoint = parentView.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        // Set up time slot spinner
        ArrayAdapter<String> adapterTimeSlots = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, timeSlots);
        adapterTimeSlots.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeSlotsSpinner.setAdapter(adapterTimeSlots);

        timeSlotsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedTimeSlot = parentView.getItemAtPosition(position).toString();
                // Optional: Show a toast message for the selected time slot
                Toast.makeText(BookBus.this, "Selected Time Slot: " + selectedTimeSlot, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
    }

    public void reserveSeat(View view) {
        // Pass selected data to the next activity
        Intent intent = new Intent(this, ReserveSeat.class);
        intent.putExtra("startPoint", selectedStartPoint);
        intent.putExtra("destinationPoint", selectedDestinationPoint);
        intent.putExtra("timeSlot", selectedTimeSlot);
        intent.putExtra("noSeats", noSeats);
        startActivity(intent);
    }
}
