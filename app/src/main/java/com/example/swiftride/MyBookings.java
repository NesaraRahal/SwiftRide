package com.example.swiftride;

import static com.example.swiftride.MainActivity.loginMail;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class MyBookings extends Activity {

    private ListView bookingsListView;
    private List<String> userBookings;
    private DBHandler dbHelper;
    private int userNic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_bookings);

        dbHelper = new DBHandler(this);
        userNic = dbHelper.getUserNic(loginMail); // Get user NIC

        bookingsListView = findViewById(R.id.bookingsListView);

        // Fetch user's bookings from DB
        userBookings = dbHelper.getBookingsByUser(userNic);

        // Set up the ListView with the user's bookings
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, userBookings);
        bookingsListView.setAdapter(adapter);

        // Set up onItemClickListener to handle cancellation
        bookingsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedBooking = userBookings.get(position); // Get selected booking details
                int seatNumber = extractSeatNumberFromBooking(selectedBooking); // Extract seat number (you can define this logic)

                // Call cancelBooking method
                cancelBooking(seatNumber);
            }
        });
    }

    private void cancelBooking(int seatNumber) {
        // Confirm cancellation with the user
        new android.app.AlertDialog.Builder(this)
                .setTitle("Cancel Booking")
                .setMessage("Do you want to cancel your booking for Seat " + seatNumber + "?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    if (dbHelper.isSeatBookedByUser(seatNumber, userNic)) {
                        dbHelper.cancelSeatBooking(seatNumber, userNic);

                        // Send cancellation email
                        String subject = "Booking Cancellation Confirmation";
                        String body = "Your booking for Seat " + seatNumber + " has been successfully canceled.";
                        EmailSender emailSender = new EmailSender(loginMail, subject, body);
                        emailSender.sendEmail();

                        // Notify the user
                        Toast.makeText(this, "Booking for Seat " + seatNumber + " has been canceled.", Toast.LENGTH_SHORT).show();
                        // Refresh the list to reflect the canceled booking
                        refreshBookingList();
                    } else {
                        Toast.makeText(this, "You can only cancel your own bookings.", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", (dialog, which) -> {
                    Toast.makeText(this, "Cancellation aborted for Seat " + seatNumber, Toast.LENGTH_SHORT).show();
                })
                .setCancelable(false)
                .show();
    }

    private void refreshBookingList() {
        // Refresh the list after cancellation
        userBookings = dbHelper.getBookingsByUser(userNic);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, userBookings);
        bookingsListView.setAdapter(adapter);
    }

    private int extractSeatNumberFromBooking(String bookingDetails) {
        // Implement logic to extract seat number from the booking string (if necessary)
        // For example, if the booking details are formatted like "Seat 1, Date: ...", you can extract seat number from here
        String[] details = bookingDetails.split(",");
        String seatPart = details[0]; // "Seat 1"
        return Integer.parseInt(seatPart.split(" ")[1]);
    }
}
