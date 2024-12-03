package com.example.swiftride;

import static com.example.swiftride.MainActivity.loginMail;
import static com.example.swiftride.ReserveSeat.busId;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NotificationSwapActivity extends Activity {

    private RecyclerView recyclerView;
    private int userNic; // This would be dynamically set based on the logged-in user
    private int selectedBusId; // Assuming you are passing the selected bus ID
    private NotificationAdapter adapter;
    private List<Notification> notifications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_swap); // Corrected layout name

        recyclerView = findViewById(R.id.recyclerViewNotifications);
        DBHandler dbHelper = new DBHandler(this);

        // Retrieve the logged-in user's NIC from the database using their email
        userNic = dbHelper.getUserNic(loginMail);
        selectedBusId = busId;

        // Display notifications for the user
        displayNotifications(userNic);
    }

    public void displayNotifications(int userId) {
        DBHandler dbHelper = new DBHandler(this);

        // Check if the user has booked a seat
        if (dbHelper.hasBookedSeat(userNic, selectedBusId)) {
            // Get notifications for the user
            notifications = dbHelper.getUserNotifications(userNic);

            // Set up RecyclerView to display notifications
            adapter = new NotificationAdapter(
                    this,
                    notifications,
                    this::acceptSwap,
                    this::rejectSwap
            );
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);
        } else {
            Toast.makeText(this, "No seat bookings found for the selected bus.", Toast.LENGTH_SHORT).show();
        }
    }

    // Corrected method signature to align with NotificationAdapter's callback
    public void acceptSwap(int notificationId) {
        DBHandler dbHelper = new DBHandler(this);

        // Retrieve the seat details from the notification
        Notification notification = findNotificationById(notificationId);
        if (notification != null) {
            int seat1 = notification.getSeat1();
            int seat2 = notification.getSeat2();
            int receiverId = notification.getReceiverId();
            int senderId = notification.getSenderId();
            int busId = notification.getBusId();

            // Perform the seat swap in the database
            dbHelper.swapSeats(this, seat1, seat2, receiverId, senderId, busId);

            // Show confirmation
            Toast.makeText(this, "Seat swap accepted", Toast.LENGTH_SHORT).show();

            // Remove notification from the list in the adapter
            int position = findNotificationPositionById(notificationId);
            if (position != -1) {
                adapter.removeNotification(position);  // Remove the notification from the list in the adapter
            }

            // Refresh the RecyclerView
            adapter.notifyDataSetChanged();
        }
    }

    public void rejectSwap(int notificationId) {
        DBHandler dbHelper = new DBHandler(this);

        // Update the notification status to rejected
        // dbHelper.updateNotificationStatus(notificationId, "Rejected");

        // Show confirmation
        Toast.makeText(this, "Seat swap rejected", Toast.LENGTH_SHORT).show();

        // Refresh notifications
        refreshNotifications();
    }

    private Notification findNotificationById(int notificationId) {
        for (Notification notification : notifications) {
            if (notification.getNotificationId() == notificationId) {
                return notification;
            }
        }
        return null;
    }

    private int findNotificationPositionById(int notificationId) {
        for (int i = 0; i < notifications.size(); i++) {
            if (notifications.get(i).getNotificationId() == notificationId) {
                return i;
            }
        }
        return -1; // Return -1 if not found
    }

    private void refreshNotifications() {
        DBHandler dbHelper = new DBHandler(this);

        // Reload the notifications from the database
        notifications.clear();
        notifications.addAll(dbHelper.getUserNotifications(userNic));

        // Notify the adapter about the data change
        adapter.notifyDataSetChanged();
    }
}
