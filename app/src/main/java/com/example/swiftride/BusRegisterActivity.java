package com.example.swiftride;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

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
                // Handle no selection (if needed)
            }
        });
    }

    public void regBus(View view) {
        EditText routeNoEdt = findViewById(R.id.routeNo);
        int routeNo = Integer.parseInt(routeNoEdt.getText().toString());

        EditText routeStartEdt = findViewById(R.id.routeStartRegister);
        String routeStart = routeStartEdt.getText().toString();

        EditText routeDestinationEdt = findViewById(R.id.routeDestination);
        String routeDestination = routeDestinationEdt.getText().toString();

        EditText seatNumberEdt = findViewById(R.id.seatNumberRegister);
        String seatNumber = seatNumberEdt.getText().toString();

        Spinner busTypeSpinner = findViewById(R.id.busTypeRegister);
        String busType = busTypeSpinner.getSelectedItem().toString();

        // Use selectedDriverNic, which holds the selected driver NIC from the spinner
        String selectedDriver = selectedDriverNic;
    }
}
