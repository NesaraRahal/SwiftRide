package com.example.swiftride;

import static com.example.swiftride.MainActivity.loginMail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

public class DriverFragment extends Fragment {

    private ListView passengerDetailsListView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the fragment layout
        View view = inflater.inflate(R.layout.driver_dashboard, container, false);

        // Initialize UI components
        passengerDetailsListView = view.findViewById(R.id.passengerDetailsListView);

        // Set up data
        setupPassengerDetails();

        return view;
    }

    private void setupPassengerDetails() {
        DBHandler dbHandler = new DBHandler(requireContext());

        // Get user NIC based on logged-in email
        int userNic = dbHandler.getUserNic(loginMail);

        // Retrieve passenger details for the driver
        List<ReservationInfo> reservationInfoList = dbHandler.getPassengerDetailsForDriver(userNic);

        // Set up the adapter for the ListView
        ReservationInfoAdapter adapter = new ReservationInfoAdapter(requireContext(), reservationInfoList);
        passengerDetailsListView.setAdapter(adapter);
    }
}
