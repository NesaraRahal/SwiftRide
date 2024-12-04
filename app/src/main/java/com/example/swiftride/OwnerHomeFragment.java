package com.example.swiftride;

import static com.example.swiftride.MainActivity.loginMail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;

import java.util.List;

public class OwnerHomeFragment extends Fragment {

    private String selectedDriverNic; // Class-level variable to store selected driver NIC

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_owner_home, container, false);

        // Initialize the driver spinner and set up the adapter
        Spinner driverSpinner = rootView.findViewById(R.id.driverEmailRegister);
        DBHandler dbHelper = new DBHandler(getActivity());
        List<String> driverNic = dbHelper.getDriverNic();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, driverNic);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        driverSpinner.setAdapter(adapter);

        // Set OnItemSelectedListener for the spinner
        driverSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedDriverNic = parentView.getItemAtPosition(position).toString(); // Store selected NIC
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        // Set the OnClickListener for the register bus button
        MaterialButton registerBusButton = rootView.findViewById(R.id.registerBusButton);
        registerBusButton.setOnClickListener(v -> regBus(rootView));

        return rootView;
    }

    public void regBus(View view) {
        // Retrieve data from input fields
        EditText licenseNoedt = view.findViewById(R.id.licenceNo);
        String licenseNo = licenseNoedt.getText().toString();

        EditText routeNoEdt = view.findViewById(R.id.routeNo);
        String routeNo = routeNoEdt.getText().toString();

        EditText routeStartEdt = view.findViewById(R.id.routeStartRegister);
        String routeStart = routeStartEdt.getText().toString();

        EditText routeDestinationEdt = view.findViewById(R.id.routeDestination);
        String routeDestination = routeDestinationEdt.getText().toString();

        EditText seatNumberEdt = view.findViewById(R.id.seatNumberRegister);
        String seatNumber = seatNumberEdt.getText().toString();

        EditText timeSlots = view.findViewById(R.id.timeSlotRegister);
        String time = timeSlots.getText().toString();

        // Parse seat number to integer
        int seatNo = Integer.parseInt(seatNumber);

        // Use selectedDriverNic, which holds the selected driver NIC from the spinner
        String selectedDriver = selectedDriverNic;
        int selectDriverId = Integer.parseInt(selectedDriver);

        // Display selected driver NIC
        Toast.makeText(getActivity(), "selectedDriver " + selectedDriver, Toast.LENGTH_SHORT).show();

        // Retrieve owner verification email
        EditText ownerVerificationEdt = view.findViewById(R.id.ownerVerificationRegister);
        String ownerVerification = ownerVerificationEdt.getText().toString();
        Toast.makeText(getActivity(), "user email " + loginMail, Toast.LENGTH_SHORT).show();

        // Validate if the owner verification matches the logged-in user's email
        if (ownerVerification.equals(loginMail)) {
            DBHandler dbHandler = new DBHandler(getActivity());

            // Retrieve owner ID using email
            int ownerId = dbHandler.getOwnerIdByEmail(loginMail);

            if (routeNo.isEmpty() || routeStart.isEmpty() || routeDestination.isEmpty() || seatNumber.isEmpty()) {
                Toast.makeText(getActivity(), "Please fill the above fields", Toast.LENGTH_SHORT).show();
            } else {
                DBHandler dbHandlerRegBus = new DBHandler(getActivity());
                long result = dbHandlerRegBus.regBus(getActivity(), licenseNo, routeNo, routeStart, routeDestination, seatNo, time, ownerId, selectDriverId);
                if (result != -1) {
                    Toast.makeText(getActivity(), "Bus registered successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Bus registration failed", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(getActivity(), "Invalid email for verification", Toast.LENGTH_SHORT).show();
        }
    }
}
