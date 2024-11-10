package com.example.swiftride;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class OwnerDashboardActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owner_dashboard);
    }

    public void addBus(View view){
        Intent intent = new Intent(this, BusRegisterActivity.class);
        startActivity(intent);
    }
}
