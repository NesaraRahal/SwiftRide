package com.example.swiftride;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.List;

public class BusRegisterActivity extends Activity {
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
    }
}