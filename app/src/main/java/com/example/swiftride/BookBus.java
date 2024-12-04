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

    public static String selectedStartPoint;
    public static String selectedDestinationPoint;
    public static String selectedTimeSlot;
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

        // Set up start point spinner
        ArrayAdapter<String> adapterStart = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, startPoint);
        adapterStart.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        startSpinner.setAdapter(adapterStart);

        startSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedStartPoint = parentView.getItemAtPosition(position).toString();

                // Update seats when all selections are available
                updateSeats(dbHelper, noOfBusTxt);
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

                // Update seats when all selections are available
                updateSeats(dbHelper, noOfBusTxt);
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

                // Update seats when all selections are available
                updateSeats(dbHelper, noOfBusTxt);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
    }

    private void updateSeats(DBHandler dbHelper, TextView noOfBusTxt) {
        // Ensure all selected values are available
        if (selectedStartPoint != null && selectedDestinationPoint != null && selectedTimeSlot != null) {
            // Directly call getNoSeats with selected parameters
            int noSeats = dbHelper.getNoSeats(selectedStartPoint, selectedDestinationPoint, selectedTimeSlot);

            // Update the TextView with the number of seats
            noOfBusTxt.setText(String.valueOf(noSeats));
        } else {
            // Set a default message if selections are incomplete
            noOfBusTxt.setText("Please select all options");
        }
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

    public void myBooks(View view){
        Intent intent = new Intent(this, MyBookings.class);
        startActivity(intent);
    }

    public void viewNotification(View view){
        Intent intent = new Intent(this, NotificationSwapActivity.class);
        startActivity(intent);
    }

    public void viewHistory(View view){
        Intent intent = new Intent(this, HistoryFeedback.class);
        startActivity(intent);
    }



}
