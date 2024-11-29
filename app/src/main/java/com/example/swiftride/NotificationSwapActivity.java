package com.example.swiftride;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class NotificationSwapActivity extends Activity {

    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notifications_swap);


        // Retrieve data from the intent
        Intent intent = getIntent();
        String action = intent.getStringExtra("action");
        int seatId1 = intent.getIntExtra("seatId1", -1);
        int seatId2 = intent.getIntExtra("seatId2", -1);
        int userNic1 = intent.getIntExtra("userNic1", -1);
        int userNic2 = intent.getIntExtra("userNic2", -1);
        int busId = intent.getIntExtra("busId", -1);

        DBHandler dbHelper = new DBHandler(this);

        if (action != null && seatId1 != -1 && seatId2 != -1 && userNic1 != -1 && userNic2 != -1) {
            if (action.equals("accept")) {
                // Handle seat swap acceptance
                dbHelper.swapSeats(this, seatId1, seatId2, userNic1, userNic2, busId);
                Toast.makeText(this, "Seat swap accepted for Seat " + seatId1, Toast.LENGTH_SHORT).show();
                finish(); // Close the activity after handling
            } else if (action.equals("decline")) {
                // Handle seat swap rejection
                Toast.makeText(this, "Seat swap declined for Seat " + seatId1, Toast.LENGTH_SHORT).show();
                finish(); // Close the activity after handling
            }
        } else {
            Toast.makeText(this, "Invalid data for seat swap.", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity
        }
    }*/
}
