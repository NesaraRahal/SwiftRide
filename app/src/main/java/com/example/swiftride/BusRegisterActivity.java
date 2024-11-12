package com.example.swiftride;

import static com.example.swiftride.MainActivity.loginMail;
import static com.example.swiftride.RegisterActivity.userEmail;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

public class BusRegisterActivity extends Activity {

    private String selectedDriverNic; // Class-level variable to store selected driver NIC

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bus_register_activity);

        Spinner driverSpinner = findViewById(R.id.driverEmailRegister);

        DBHandler dbHelper = new DBHandler(this);
        List<String> driverNic = dbHelper.getDriverNic();

        // Create an ArrayAdapter using the driver names and set it to the spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, driverNic);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        driverSpinner.setAdapter(adapter);

        // Set the OnItemSelectedListener to handle item selection in the spinner
        driverSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedDriverNic = parentView.getItemAtPosition(position).toString(); // Assign selected NIC to class-level variable
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
    }

    public void regBus(View view) {

            EditText licenseNoedt = findViewById(R.id.licenceNo);
            String licenseNo = licenseNoedt.getText().toString();

            EditText routeNoEdt = findViewById(R.id.routeNo);
            String routeNo = routeNoEdt.getText().toString();

            EditText routeStartEdt = findViewById(R.id.routeStartRegister);
            String routeStart = routeStartEdt.getText().toString();

            EditText routeDestinationEdt = findViewById(R.id.routeDestination);
            String routeDestination = routeDestinationEdt.getText().toString();

            EditText seatNumberEdt = findViewById(R.id.seatNumberRegister);
            String seatNumber = seatNumberEdt.getText().toString();

            int seatNo = Integer.parseInt(seatNumber);


        // Use selectedDriverNic, which holds the selected driver NIC from the spinner
            String selectedDriver = selectedDriverNic;
            int selectDriverId = Integer.parseInt(selectedDriver);

            Toast.makeText(this, "selectedDriver"+ selectedDriver, Toast.LENGTH_SHORT).show();

            EditText ownerVerificationEdt = findViewById(R.id.ownerVerificationRegister);
            String ownerVerification = ownerVerificationEdt.getText().toString();
            Toast.makeText(this, "user email"+ loginMail, Toast.LENGTH_SHORT).show();

        if(ownerVerification.equals(loginMail)){
                // Write a query to retiveve owner id using email
                DBHandler dbHandler = new DBHandler(this);

                // Retrieve owner ID using email
                int ownerId = dbHandler.getOwnerIdByEmail(loginMail);

                if(routeNo.isEmpty() || routeStart.isEmpty() || routeDestination.isEmpty() || seatNumber.isEmpty() ){
                    Toast.makeText(this, "Please fill the above fields", Toast.LENGTH_SHORT).show();
                }

                DBHandler dbHandlerRegBus = new DBHandler(this);
                long result = dbHandlerRegBus.regBus(this, licenseNo, routeNo, routeStart, routeDestination, seatNo, ownerId, selectDriverId);
                if (result != -1) {
                    Toast.makeText(this, "Bus registered successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Bus registration failed", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(this, "Invalid email for verification", Toast.LENGTH_SHORT).show();
            }



    }
}
