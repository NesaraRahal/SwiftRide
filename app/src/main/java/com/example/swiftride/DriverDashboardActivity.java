package com.example.swiftride;

import static com.example.swiftride.MainActivity.loginMail;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DriverDashboardActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_dashboard);

        ListView passengerDetailsListView = findViewById(R.id.passengerDetailsListView);

        DBHandler dbHandler = new DBHandler(this);
        int userNic = dbHandler.getUserNic(loginMail);

        List<ReservationInfo> reservationInfoList = dbHandler.getPassengerDetailsForDriver(userNic);

        ReservationInfoAdapter adapter = new ReservationInfoAdapter(this, reservationInfoList);
        passengerDetailsListView.setAdapter(adapter);
    }
}
