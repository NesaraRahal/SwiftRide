package com.example.swiftride;

import static com.example.swiftride.BookBus.selectedDestinationPoint;
import static com.example.swiftride.BookBus.selectedStartPoint;
import static com.example.swiftride.BookBus.selectedTimeSlot;
import static com.example.swiftride.MainActivity.loginMail;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ReserveSeat extends Activity {

    private int totalSeats = 0; // Example seat count (can be fetched dynamically from DB)
    private List<Integer> bookedSeats = new ArrayList<>(); // Mock data for booked seats

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reserve_seat);



        DBHandler dbHelper = new DBHandler(this);
        totalSeats = dbHelper.getNoSeats(selectedStartPoint, selectedDestinationPoint, selectedTimeSlot);
        GridLayout seatGrid = findViewById(R.id.seatGrid);
        int userNic = dbHelper.getUserNic(loginMail);
        int driverId = dbHelper.getDriverId(selectedStartPoint, selectedDestinationPoint, selectedTimeSlot);
        int ownerId =  dbHelper.getOwnerId(selectedStartPoint, selectedDestinationPoint, selectedTimeSlot);
        int busId = dbHelper.getBusId(selectedStartPoint, selectedDestinationPoint, selectedTimeSlot);


                // Mock booked seats (you can fetch this from the database)
        bookedSeats.add(3);
        bookedSeats.add(7);
        bookedSeats.add(15);

        // Dynamically create seat buttons
        for (int i = 1; i <= totalSeats; i++) {
            Button seatButton = new Button(this);
            seatButton.setText("Seat " + i);
            seatButton.setId(i);

            // Mark the seat as booked or available
            if (bookedSeats.contains(i)) {
                seatButton.setBackgroundColor(Color.RED); // Booked seats in red
                seatButton.setEnabled(false); // Disable interaction for booked seats
            } else {
                seatButton.setBackgroundColor(Color.GREEN); // Available seats in green
            }

            // Add click listener for available seats
            seatButton.setOnClickListener(view -> {
                seatButton.setBackgroundColor(Color.YELLOW); // Mark as selected
                new android.app.AlertDialog.Builder(ReserveSeat.this)
                        .setTitle("Confirm Booking")
                        .setMessage("Do you want to book Seat " + seatButton.getId() + "?")
                        .setPositiveButton("Yes", (dialog, which) -> {

                            String currentDateAndTime = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault())
                                    .format(new java.util.Date());
                            // Action for confirmed booking
                            int bookedSeat =  seatButton.getId();
                            Toast.makeText(ReserveSeat.this, "Seat " + bookedSeat + " Date: " + currentDateAndTime +" booked successfully!", Toast.LENGTH_SHORT).show();
                            insertReserve(currentDateAndTime, busId, userNic, driverId, ownerId, selectedStartPoint, selectedDestinationPoint, bookedSeat);
                            // Update the seat's color to indicate it is now booked
                            seatButton.setBackgroundColor(Color.RED);
                            seatButton.setEnabled(false); // Disable the button
                        })
                        .setNegativeButton("No", (dialog, which) -> {
                            // Reset the seat's color if booking is canceled
                            seatButton.setBackgroundColor(Color.GREEN);
                            Toast.makeText(ReserveSeat.this, "Booking canceled for Seat " + seatButton.getId(), Toast.LENGTH_SHORT).show();
                        })
                        .setCancelable(false) // Prevent dialog dismissal by clicking outside
                        .show();            });

            // Add the button to the GridLayout
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.setMargins(8, 8, 8, 8);
            seatButton.setLayoutParams(params);
            seatGrid.addView(seatButton);
        }


    }

    public void insertReserve(String reserveDate, int busId, int passengerId, int driverId, int ownerId, String start, String destination, int seatNo) {
        DBHandler dbHelper = new DBHandler(this);

        // Call the reserveSeat method from DBHandler to insert the reservation data
        long result = dbHelper.reserveSeat(this, reserveDate, busId, passengerId, driverId, ownerId, start, destination, seatNo);

        if (result != -1) {
            Toast.makeText(this, "Reservation made successfully!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Reservation failed", Toast.LENGTH_SHORT).show();
        }
    }




}