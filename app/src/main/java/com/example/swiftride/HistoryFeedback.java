package com.example.swiftride;

import static com.example.swiftride.MainActivity.loginMail;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HistoryFeedback extends Activity {

    private RecyclerView bookingRecyclerView;
    private FeedbackAdapter feedbackAdapter;
    private List<Booking> bookingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_feedback);

        // Initialize RecyclerView
        bookingRecyclerView = findViewById(R.id.bookingRecyclerView);
        bookingRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the DBHandler to fetch user's bookings
        DBHandler dbHelper = new DBHandler(this);

        // Get the user ID based on the logged-in email (loginMail)
        int userNic = dbHelper.getUserNic(loginMail);

        if (userNic != -1) {
            // Get the user's bookings from the database
            bookingList = dbHelper.getUserBookings(userNic);

            if (bookingList.isEmpty()) {
                // If no bookings found, show a message
                Toast.makeText(this, "No bookings found!", Toast.LENGTH_SHORT).show();
            } else {
                // Create and set the adapter to the RecyclerView
                feedbackAdapter = new FeedbackAdapter(this, bookingList);
                bookingRecyclerView.setAdapter(feedbackAdapter);
            }
        } else {
            // Handle case when user is not found
            Toast.makeText(this, "User not found!", Toast.LENGTH_SHORT).show();
        }
    }
}
