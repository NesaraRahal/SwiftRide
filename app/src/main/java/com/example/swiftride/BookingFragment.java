package com.example.swiftride;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

public class BookingFragment extends Fragment {

    public static String selectedStartPoint;
    public static String selectedDestinationPoint;
    public static String selectedTimeSlot;
    private int noSeats;

    private DBHandler dbHelper;
    private TextView noOfBusTxt;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_booking, container, false);

        // Initialize spinners and TextView
        Spinner startSpinner = rootView.findViewById(R.id.selectBusStart);
        Spinner destinationSpinner = rootView.findViewById(R.id.selectBusDestination);
        Spinner timeSlotsSpinner = rootView.findViewById(R.id.selectBusTime);
        noOfBusTxt = rootView.findViewById(R.id.no_seats);

        // Initialize database helper
        dbHelper = new DBHandler(requireContext());

        // Retrieve data from the database
        List<String> startPoint = dbHelper.getStartPoint();
        List<String> destinationPoint = dbHelper.getDestination();
        List<String> timeSlots = dbHelper.getDistinctTimeSlots();

        // Set up start point spinner
        ArrayAdapter<String> adapterStart = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, startPoint);
        adapterStart.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        startSpinner.setAdapter(adapterStart);

        startSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedStartPoint = parentView.getItemAtPosition(position).toString();

                // Update seats when all selections are available
                updateSeats();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        // Set up destination point spinner
        ArrayAdapter<String> adapterDestination = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, destinationPoint);
        adapterDestination.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        destinationSpinner.setAdapter(adapterDestination);

        destinationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedDestinationPoint = parentView.getItemAtPosition(position).toString();

                // Update seats when all selections are available
                updateSeats();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        // Set up time slot spinner
        ArrayAdapter<String> adapterTimeSlots = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, timeSlots);
        adapterTimeSlots.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeSlotsSpinner.setAdapter(adapterTimeSlots);

        timeSlotsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedTimeSlot = parentView.getItemAtPosition(position).toString();

                // Update seats when all selections are available
                updateSeats();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        // Set up button listeners
        rootView.findViewById(R.id.reserveSeat).setOnClickListener(this::reserveSeat);
        rootView.findViewById(R.id.myBookings).setOnClickListener(this::myBooks);
        rootView.findViewById(R.id.myNotifications).setOnClickListener(this::viewNotification);
        rootView.findViewById(R.id.viewPast).setOnClickListener(this::viewHistory);

        return rootView;
    }

    private void updateSeats() {
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
        Intent intent = new Intent(getActivity(), ReserveSeat.class);
        intent.putExtra("startPoint", selectedStartPoint);
        intent.putExtra("destinationPoint", selectedDestinationPoint);
        intent.putExtra("timeSlot", selectedTimeSlot);
        intent.putExtra("noSeats", noSeats);
        startActivity(intent);
    }

    public void myBooks(View view) {
        Intent intent = new Intent(getActivity(), MyBookings.class);
        startActivity(intent);
    }

    public void viewNotification(View view) {
        Intent intent = new Intent(getActivity(), NotificationSwapActivity.class);
        startActivity(intent);
    }

    public void viewHistory(View view) {
        Intent intent = new Intent(getActivity(), HistoryFeedback.class);
        startActivity(intent);
    }
}
