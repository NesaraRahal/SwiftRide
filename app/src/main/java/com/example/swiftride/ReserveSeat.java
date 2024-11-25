package com.example.swiftride;

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

    private int totalSeats = 20; // Example seat count (can be fetched dynamically from DB)
    private List<Integer> bookedSeats = new ArrayList<>(); // Mock data for booked seats

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reserve_seat);

        GridLayout seatGrid = findViewById(R.id.seatGrid);

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
                Toast.makeText(ReserveSeat.this, "You selected Seat " + seatButton.getId(), Toast.LENGTH_SHORT).show();
            });

            // Add the button to the GridLayout
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.setMargins(8, 8, 8, 8);
            seatButton.setLayoutParams(params);
            seatGrid.addView(seatButton);
        }


    }



}
