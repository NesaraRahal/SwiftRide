package com.example.swiftride;

import static com.example.swiftride.BookBus.selectedDestinationPoint;
import static com.example.swiftride.BookBus.selectedStartPoint;
import static com.example.swiftride.BookBus.selectedTimeSlot;
import static com.example.swiftride.MainActivity.loginMail;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReserveSeat extends Activity {

    private int totalSeats = 0; // Example seat count (can be fetched dynamically from DB)

    private int seat1 = 0;
    private List<Integer> bookedSeats = new ArrayList<>(); // Dynamically fetched booked seats

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reserve_seat);

        DBHandler dbHelper = new DBHandler(this);

        // Fetch total seats for the selected bus
        totalSeats = dbHelper.getNoSeats(selectedStartPoint, selectedDestinationPoint, selectedTimeSlot);
        if (totalSeats <= 0) {
            Toast.makeText(this, "No seats available for the selected bus!", Toast.LENGTH_SHORT).show();
            return; // Exit if no seats are available
        }

        // Fetch dynamically the booked seats for the selected bus and date
        String currentDate = new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault()).format(new java.util.Date());
        bookedSeats = dbHelper.getBookedSeats(selectedStartPoint, selectedDestinationPoint, selectedTimeSlot, currentDate);

        // Initialize seat grid layout
        GridLayout seatGrid = findViewById(R.id.seatGrid);
        int userNic = dbHelper.getUserNic(loginMail);
        int driverId = dbHelper.getDriverId(selectedStartPoint, selectedDestinationPoint, selectedTimeSlot);
        int ownerId = dbHelper.getOwnerId(selectedStartPoint, selectedDestinationPoint, selectedTimeSlot);
        int busId = dbHelper.getBusId(selectedStartPoint, selectedDestinationPoint, selectedTimeSlot);

        // Dynamically create seat buttons
        for (int i = 1; i <= totalSeats; i++) {
            Button seatButton = new Button(this);
            seatButton.setText("Seat " + i);
            seatButton.setId(i);

            // Mark the seat as booked or available
            if (bookedSeats != null && bookedSeats.contains(i)) {
                seatButton.setBackgroundColor(Color.RED); // Booked seats in red
                addSwappingListener(seatButton, loginMail, userNic, dbHelper, busId, driverId, ownerId);
            } else {
                seatButton.setBackgroundColor(Color.GREEN); // Available seats in green
                addBooking(seatButton, busId, userNic, driverId, ownerId, selectedStartPoint, selectedDestinationPoint);
            }

            // Add the button to the GridLayout
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.setMargins(8, 8, 8, 8);
            seatButton.setLayoutParams(params);
            seatGrid.addView(seatButton);
        }
    }

    public void addBooking(Button seatButton, int busId, int userNic, int driverId, int ownerId, String selectedStartPoint, String selectedDestinationPoint) {
        seatButton.setOnClickListener(view -> {
            seatButton.setBackgroundColor(Color.YELLOW); // Mark as selected
            new android.app.AlertDialog.Builder(ReserveSeat.this)
                    .setTitle("Confirm Booking")
                    .setMessage("Do you want to book Seat " + seatButton.getId() + "?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        String currentDateAndTime = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault())
                                .format(new java.util.Date());
                        int bookedSeat = seatButton.getId();

                        // Insert booking into the database
                        insertReserve(currentDateAndTime, busId, userNic, driverId, ownerId, selectedStartPoint, selectedDestinationPoint, bookedSeat);

                        // Send confirmation email
                        String subject = "Bus Reservation Confirmation";
                        String body = "Dear User,\n\nYour bus reservation has been successfully made for Seat " + bookedSeat + ".\n\nThank you for choosing our service!";
                        EmailSender emailSender = new EmailSender(loginMail, subject, body);
                        emailSender.sendEmail();

                        // Update the seat's color to indicate it is now booked
                        seatButton.setBackgroundColor(Color.RED);
                        seatButton.setEnabled(false); // Disable the button
                        seat1 = seatButton.getId();
                    })
                    .setNegativeButton("No", (dialog, which) -> {
                        seatButton.setBackgroundColor(Color.GREEN); // Reset seat to available
                        Toast.makeText(ReserveSeat.this, "Booking canceled for Seat " + seatButton.getId(), Toast.LENGTH_SHORT).show();
                    })
                    .setCancelable(false)
                    .show();
        });
    }

    public void addSwappingListener(Button seatButton, String loginMail, int userNic, DBHandler dbHelper, int busId, int driverId, int ownerId) {
        seatButton.setOnClickListener(view -> {
            new android.app.AlertDialog.Builder(ReserveSeat.this)
                    .setTitle("Seat Swapping")
                    .setMessage("Do you want to swap the Seat " + seatButton.getId() + "?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        if (!dbHelper.isSeatBookedByUser(seatButton.getId(), userNic)) {

                            int seatId2 = seatButton.getId();
                            int user2 = dbHelper.getPassengerId(seatId2, selectedStartPoint, selectedDestinationPoint, selectedTimeSlot);

                            String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
                            dbHelper.createNotification(userNic, user2, seat1, seatId2, busId, "Pending", currentTime);

                            //dbHelper.swapSeats(this, seat1, seatId2, userNic, user2, busId);

                            // Send cancellation email
                            String subject = "Seat Swap Confimation";
                            String body = "Your booking for Seat " + seatButton.getId() + " has been successfully canceled.";
                            EmailSender emailSender = new EmailSender(loginMail, subject, body);
                            emailSender.sendEmail();



                        } else {
                            Toast.makeText(ReserveSeat.this, "Seat Swapped successfully", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("No", (dialog, which) -> {
                        Toast.makeText(ReserveSeat.this, "Swapping aborted for Seat " + seatButton.getId(), Toast.LENGTH_SHORT).show();
                    })
                    .setCancelable(false)
                    .show();
        });
    }



    public void insertReserve(String reserveDate, int busId, int passengerId, int driverId, int ownerId, String start, String destination, int seatNo) {
        DBHandler dbHelper = new DBHandler(this);
        long result = dbHelper.reserveSeat(this, reserveDate, busId, passengerId, driverId, ownerId, start, destination, seatNo, selectedTimeSlot);

        if (result != -1) {
            Toast.makeText(this, "Reservation made successfully!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Reservation failed", Toast.LENGTH_SHORT).show();
        }
    }
}
